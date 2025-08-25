package com.dndn.backend.dndn.domain.welfare.application;

import com.dndn.backend.dndn.domain.category.application.CategoryService;
import com.dndn.backend.dndn.domain.category.domain.Category;
import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import com.dndn.backend.dndn.domain.welfare.domain.enums.SourceType;
import com.dndn.backend.dndn.domain.welfare.domain.repository.WelfareRepository;
import com.dndn.backend.dndn.domain.welfareOpenApi.local.client.LocalWelfareClient;
import com.dndn.backend.dndn.domain.welfareOpenApi.local.dto.response.LocalDetailResDto;
import com.dndn.backend.dndn.domain.welfareOpenApi.local.dto.response.LocalListResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.dndn.backend.dndn.domain.category.util.CategoryParserUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalWelfareSyncService {

    private final WelfareRepository welfareRepository;
    private final LocalWelfareClient localClient;
    private final CategoryService categoryService;

    public void syncLocalWelfareData() {
        int page = 1;
        int numOfRows = 100;

        while (true) {
            LocalListResDto listDto = localClient.getWelfareList(page, numOfRows);
            if (listDto == null || listDto.getServList() == null || listDto.getServList().isEmpty()) {
                log.info("[지자체 동기화] {} 페이지에 더 이상 데이터 없음. 종료", page);
                break;
            }

            log.info("[지자체 동기화] {} 페이지, {}건 처리 시작", page, listDto.getServList().size());

            for (LocalListResDto.ServiceItem item : listDto.getServList()) {
                String servId = item.getServId();
                if (isBlank(servId)) continue;

                // 상세 조회
                LocalDetailResDto detail = localClient.getWelfareDetail(servId);
                if (detail == null) {
                    log.warn("[지자체 동기화] 상세 조회 실패 servId={}", servId);
                    continue;
                }

                // ✅ 상세 DTO 값 로그 확인
                log.info("servId={} servDgst={}", servId, detail.getServDgst());
                log.info("servId={} basfrmList from API: {}", servId, detail.getBasfrmList());
                log.info("servId={} aplyDocList from API: {}", servId, detail.getAplyDocList());

                // 카테고리 매핑
                String lifeSrc  = nzOr(detail.getLifeNmArray(), item.getLifeNmArray());
                String hhSrc    = nzOr(detail.getTrgterIndvdlNmArray(), item.getTrgterIndvdlNmArray());
                String intrsSrc = nzOr(detail.getIntrsThemaNmArray(), item.getIntrsThemaNmArray());

                List<LifeCycle> life = parseLifeCycles(nz(lifeSrc));
                List<HouseholdType> hh = parseHouseholdTypes(nz(hhSrc));
                List<InterestTopic> it = parseInterestTopics(nz(intrsSrc));
                Category category = categoryService.findOrCreateCategory(life, hh, it);
                if (category == null) {
                    log.warn("[지자체 동기화] 카테고리 null (servId={})", servId);
                    continue;
                }

                String org = Optional.ofNullable(detail.getInqplCtadrList())
                        .orElse(List.of())
                        .stream()
                        .map(LocalDetailResDto.RelatedInfo::getWlfareInfoReldNm)
                        .filter(s -> s != null && !s.isBlank())
                        .findFirst() // 여러 개면 첫 번째만 사용
                        .orElse("기관 미제공");

                String detailInfo = Optional.ofNullable(detail.getBasfrmList())
                        .orElse(List.of())
                        .stream()
                        .map(LocalDetailResDto.RelatedInfo::getWlfareInfoReldCn)
                        .filter(s -> s != null && !s.isBlank())
                        .findFirst()
                        .orElse("상세정보 미제공");


                Welfare welfare = welfareRepository.findByServId(servId).orElse(null);

                if (welfare == null) {
                    welfareRepository.save(Welfare.builder()
                            .servId(servId)
                            .title(nzOr(detail.getServNm(), item.getServNm(), "제목 미제공"))
                            .summary(nzOr(detail.getServDgst(), "요약 미제공"))
                            .content(nzOr(detail.getAlwServCn(), "내용 미제공"))
                            .servLink(nz(item.getServDtlLink()))
                            .eligibleUser(nzOr(detail.getSprtTrgtCn(), "대상자 정보 미제공"))
                            .detailInfo(detailInfo)
                            .department(nzOr(detail.getBizChrDeptNm(), "담당부처 미제공"))
                            .org(org)
                            .sourceType(SourceType.LOCAL)
                            .ctpvNm(item.getCtpvNm())
                            .sggNm(item.getSggNm())
                            .category(category)
                            .build());
                } else {
                    // 새로운 값 추출
                    String newSummary      = detail.getServDgst();
                    String newContent      = nzOr(detail.getAlwServCn(), "내용 미제공");
                    String newServLink     = nz(item.getServDtlLink());
                    String newDepartment   = nzOr(detail.getBizChrDeptNm(), "담당부처 미제공");
                    String newOrg          = org;
                    String newEligibleUser = nzOr(detail.getSprtTrgtCn(), "대상자 정보 미제공");
                    String newDetailInfo   = detailInfo;

                    boolean updated = false;

                    // 주요 필드 변경 여부
                    boolean needsMainUpdate =
                            !safeEq(welfare.getSummary(),      newSummary)      ||
                                    !safeEq(welfare.getContent(),      newContent)      ||
                                    !safeEq(welfare.getServLink(),     newServLink)     ||
                                    !safeEq(welfare.getDepartment(),   newDepartment)   ||
                                    !safeEq(welfare.getOrg(),          newOrg)          ||
                                    !safeEq(welfare.getEligibleUser(), newEligibleUser) ||
                                    !safeEq(welfare.getDetailInfo(),   newDetailInfo);

                    if (needsMainUpdate) {
                        welfare.update(
                                newSummary,
                                newContent,
                                newServLink,
                                newDepartment,
                                newOrg,
                                newEligibleUser,
                                newDetailInfo
                        );
                        updated = true;
                    }

                    // 카테고리
                    if (welfare.getCategory() == null ||
                            welfare.getCategory().getId() == null ||
                            !welfare.getCategory().getId().equals(category.getId())) {
                        welfare.updateCategory(category);
                        updated = true;
                    }

                    // 지역
                    if (!safeEq(welfare.getCtpvNm(), item.getCtpvNm()) ||
                            !safeEq(welfare.getSggNm(), item.getSggNm())) {
                        welfare.updateRegion(item.getCtpvNm(), item.getSggNm());
                        updated = true;
                    }

                    if (updated) welfareRepository.save(welfare);
                }
            }

            log.info("[지자체 동기화] {} 페이지 처리 완료", page);
            page++;
        }

        log.info("[지자체 동기화] 전체 동기화 완료");
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }

    private static String nz(String s) { return s == null ? "" : s; }

    private static String nzOr(String... candidates) {
        for (String c : candidates) if (!isBlank(c)) return c;
        return "";
    }

    private boolean safeEq(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
    private boolean safeEq(LocalDateTime a, LocalDateTime b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}
