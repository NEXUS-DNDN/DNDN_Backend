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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
                if (servId == null || servId.isBlank()) continue;

                // 상세 조회(기간/대상/제출서류 확보)
                LocalDetailResDto detail = localClient.getWelfareDetail(servId);
                if (detail == null) {
                    log.warn("[지자체 동기화] 상세 조회 실패 servId={}", servId);
                    continue;
                }

                // 카테고리 매핑(상세값 우선, 없으면 목록값 사용)
                String lifeSrc  = (detail.getLifeNmArray() != null) ? detail.getLifeNmArray() : item.getLifeNmArray();
                String hhSrc    = (detail.getTrgterIndvdlNmArray() != null) ? detail.getTrgterIndvdlNmArray() : item.getTrgterIndvdlNmArray();
                String intrsSrc = (detail.getIntrsThemaNmArray() != null) ? detail.getIntrsThemaNmArray() : item.getIntrsThemaNmArray();

                List<LifeCycle> life = parseLifeCycles(lifeSrc);
                List<HouseholdType> hh = parseHouseholdTypes(hhSrc);
                List<InterestTopic> it = parseInterestTopics(intrsSrc);

                Category category = categoryService.findOrCreateCategory(life, hh, it);

                // 기간
                LocalDateTime start = parseYmd(detail.getEnfcBgngYmd());
                LocalDateTime end   = parseYmd(detail.getEnfcEndYmd());

                // 내용/대상/서류
                String content       = (detail.getServDgst() != null) ? detail.getServDgst() : item.getServDgst();
                String eligibleUser  = detail.getSprtTrgtCn();   // 지원대상
                String submitDoc     = detail.getAlwServCn();    // 제출서류/지원내용
                String servLink      = item.getServDtlLink();    // 목록에만 존재

                Welfare welfare = welfareRepository.findByServId(servId).orElse(null);

                if (welfare == null) {
                    welfareRepository.save(Welfare.builder()
                            .servId(servId)
                            .title(item.getServNm())
                            .content(content != null ? content : "")
                            .servLink(servLink)
                            .imageUrl(null)
                            .eligibleUser(eligibleUser != null ? eligibleUser : "")
                            .submitDocument(submitDoc != null ? submitDoc : "")
                            .startDate(start)
                            .endDate(end)
                            .sourceType(SourceType.LOCAL)
                            .ctpvNm(item.getCtpvNm())
                            .sggNm(item.getSggNm())
                            .category(category)
                            .build());
                } else {
                    boolean updated = false;

                    // 본문/링크/대상/서류
                    String curContent = welfare.getContent();
                    String curLink    = welfare.getServLink();
                    String curElig    = welfare.getEligibleUser();
                    String curDoc     = welfare.getSubmitDocument();

                    // 변경 체크
                    if (!safeEq(curContent, content) ||
                            !safeEq(curLink, servLink) ||
                            !safeEq(curElig, eligibleUser) ||
                            !safeEq(curDoc, submitDoc)) {

                        welfare.update(
                                content != null ? content : "",
                                servLink,
                                eligibleUser != null ? eligibleUser : "",
                                submitDoc != null ? submitDoc : ""
                        );
                        updated = true;
                    }

                    // 카테고리 변경(PK 기준)
                    if (welfare.getCategory() == null ||
                            welfare.getCategory().getId() == null ||
                            !welfare.getCategory().getId().equals(category.getId())) {
                        welfare.updateCategory(category);
                        updated = true;
                    }

                    // 지역 정보 변경
                    if (!safeEq(welfare.getCtpvNm(), item.getCtpvNm()) ||
                            !safeEq(welfare.getSggNm(), item.getSggNm())) {
                        welfare.updateRegion(item.getCtpvNm(), item.getSggNm());
                        updated = true;
                    }

                    // 기간 변경(엔티티에 period 업데이트 메서드 권장)
                    if (!safeEq(welfare.getStartDate(), start) || !safeEq(welfare.getEndDate(), end)) {
                        // Welfare 엔티티에 아래 메서드 하나 추가해 두는 걸 추천합니다.
                        // public void updatePeriod(LocalDateTime start, LocalDateTime end) { this.startDate = start; this.endDate = end; }
                        welfare.updatePeriod(start, end);
                        updated = true;
                    }

                    if (updated) {
                        welfareRepository.save(welfare);
                    }
                }
            }

            log.info("[지자체 동기화] {} 페이지 처리 완료", page);
            page++;
        }

        log.info("[지자체 동기화] {} 전체 동기화 완료");
    }

    // yyyyMMdd -> LocalDateTime(00:00)
    private LocalDateTime parseYmd(String ymd) {
        if (ymd == null || ymd.isBlank()) return null;
        LocalDate d = LocalDate.parse(ymd.trim(), DateTimeFormatter.BASIC_ISO_DATE);
        return d.atStartOfDay();
    }

    private boolean safeEq(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
    private boolean safeEq(LocalDateTime a, LocalDateTime b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}
