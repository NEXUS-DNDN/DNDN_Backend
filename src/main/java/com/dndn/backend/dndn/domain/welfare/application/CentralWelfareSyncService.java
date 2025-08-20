package com.dndn.backend.dndn.domain.welfare.application;

import com.dndn.backend.dndn.domain.category.application.CategoryService;
import com.dndn.backend.dndn.domain.category.domain.Category;
import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import com.dndn.backend.dndn.domain.welfare.domain.enums.SourceType;
import com.dndn.backend.dndn.domain.welfare.domain.repository.WelfareRepository;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.client.CentralWelfareClient;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralDetailResDto;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralListResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.dndn.backend.dndn.domain.category.util.CategoryParserUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CentralWelfareSyncService {

    private final WelfareRepository welfareRepository;
    private final CentralWelfareClient centralClient;
    private final CategoryService categoryService;

    public void syncCentralWelfareData() {
        int page = 1;
        int numOfRows = 100;

        while (true) {
            CentralListResDto reslist = centralClient.getWelfareList(page, numOfRows);
            log.info("[동기화] {}페이지 응답 도착", page);

            if (reslist == null) { log.warn("centralListResDto == null"); break; }
            // 목록 성공 코드 확인(운영 응답은 "0" 또는 "00" 케이스가 있으므로 둘 다 허용)
            if (reslist.getResultCode() != null && !("0".equals(reslist.getResultCode()) || "00".equals(reslist.getResultCode()))) {
                log.warn("목록 실패 code={}, msg={}", reslist.getResultCode(), reslist.getResultMessage());
                break;
            }

            List<CentralListResDto.ServiceItem> items = reslist.getServList();
            if (items == null || items.isEmpty()) {
                log.info("serviceItems 비어있음. 종료");
                break;
            }

            log.info("[동기화] {}개의 서비스 처리 시작", items.size());

            for (CentralListResDto.ServiceItem item : items) {
                String servId = item.getServId();
                if (isBlank(servId)) continue;

                CentralDetailResDto dtl = centralClient.getWelfareDetail(servId);
                if (dtl == null) { log.warn("상세 null (servId={})", servId); continue; }
                if (dtl.getResultCode() != null && !("0".equals(dtl.getResultCode()) || "00".equals(dtl.getResultCode()))) {
                    log.warn("상세 실패 (servId={}) code={}, msg={}", servId, dtl.getResultCode(), dtl.getResultMessage());
                    continue;
                }

                // ✅ null/blank 안전 추출 + 대체값
                String title    = nzOr(dtl.getServNm(), item.getServNm(), "제목 미제공");
                String summary = nzOr(dtl.getWlfareInfoOutlCn(), "요약 미제공");
                String content  = nzOr(dtl.getAlwServCn(), item.getServDgst(), "내용 미제공"); // ← content 절대 null 금지

                String link = Optional.ofNullable(dtl.getInqplHmpgReldList())
                        .flatMap(list -> list.stream()
                                .map(CentralDetailResDto.ServDetail::getServSeDetailLink)
                                .filter(Objects::nonNull)
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .findFirst())
                        .orElse(null);

                String detailInfo = Optional.ofNullable(dtl.getBasfrmList())
                        .flatMap(list -> list.stream()
                                .map(CentralDetailResDto.ServDetail::getServSeDetailLink)
                                .filter(Objects::nonNull)
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .findFirst())
                        .orElse(null);

                String eligible = nzOr(dtl.getTgtrDtlCn(), "대상자 정보 미제공");
                String submit   = nzOr(dtl.getAlwServCn(), "제출서류 정보 미제공");
                String dept     = nzOr(dtl.getJurMnofNm(), "담당부처 미제공");                 // department
                String org      = nzOr(dtl.getJurMnofNm(), "담당기관 미제공");                  //

                // ✅ 카테고리 매핑
                List<LifeCycle> lifeCycles     = parseLifeCycles(nz(dtl.getLifeArray()));
                List<HouseholdType> household  = parseHouseholdTypes(nz(dtl.getTrgterIndvdlArray()));
                List<InterestTopic> interests  = parseInterestTopics(nz(dtl.getIntrsThemaArray()));
                Category category = categoryService.findOrCreateCategory(lifeCycles, household, interests);
                if (category == null) { log.warn("카테고리 null (servId={})", servId); continue; }

                Welfare welfare = welfareRepository.findByServId(servId).orElse(null);

                if (welfare == null) {
                    Welfare newWelfare = Welfare.builder()
                            .servId(servId)
                            .title(title)
                            .summary(summary)
                            .content(content)
                            .servLink(link)
                            .ctpvNm("지역정보없음")
                            .sggNm("지역정보없음")
                            .department(dept)
                            .org(org)
                            .imageUrl(null)
                            .eligibleUser(eligible)
                            .submitDocument(submit)
                            .detailInfo(detailInfo)
                            .startDate(null)
                            .endDate(null)
                            .sourceType(SourceType.CENTRAL)
                            .category(category)
                            .build();
                    welfareRepository.save(newWelfare);
                } else {
                    boolean updated = false;

                    if (!Objects.equals(welfare.getContent(), content) ||
                            !Objects.equals(welfare.getServLink(), link) ||
                            !Objects.equals(welfare.getEligibleUser(), eligible) ||
                            !Objects.equals(welfare.getSubmitDocument(), submit)) {

                        welfare.update(
                                summary,      // summary
                                content,    // content
                                link,       // servLink
                                dept,   // department
                                org,    // org
                                eligible,   // eligibleUser
                                detailInfo      // detailInfo (여기에 제출서류 넣음)
                        );

                        updated = true;
                    }

                    if (welfare.getCategory() == null ||
                            !Objects.equals(welfare.getCategory().getId(), category.getId())) {
                        welfare.updateCategory(category);
                        updated = true;
                    }

                    if (isBlank(welfare.getCtpvNm()) || isBlank(welfare.getSggNm())) {
                        welfare.updateRegion("지역정보없음", "지역정보없음");
                        updated = true;
                    }

                    if (updated) welfareRepository.save(welfare);
                }
            }

            log.info("[복지 동기화] {}페이지 동기화 완료", page);
            page++;
        }

        log.info("[복지 동기화] 중앙부처 전체 동기화 완료");
    }

    /* ---------- helpers ---------- */
    private static String nz(String s) { return s == null ? "" : s; }

    private static String nzOr(String... candidates) {
        for (String c : candidates) if (!isBlank(c)) return c;
        return "";
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
}
