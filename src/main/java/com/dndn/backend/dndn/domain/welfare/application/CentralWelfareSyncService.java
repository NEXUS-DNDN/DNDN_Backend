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
            CentralListResDto centralListResDto = centralClient.getWelfareList(page, numOfRows);
            log.info("[동기화] {}페이지 응답 도착", page);

            if (centralListResDto == null) {
                log.warn("centralListResDto가 null입니다.");
                break;
            }

            log.info("resultCode: {}", centralListResDto.getResultCode());
            log.info("resultMessage: {}", centralListResDto.getResultMessage());

            List<CentralListResDto.ServiceItem> serviceItems = centralListResDto.getServList();
            if (serviceItems == null) {
                log.warn("serviceItems가 null입니다.");
                break;
            }
            if (serviceItems.isEmpty()) {
                log.warn("serviceItems가 비어 있습니다.");
                break;
            }

            log.info("[동기화] {}개의 서비스 처리 시작", serviceItems.size());

            for (CentralListResDto.ServiceItem item : serviceItems) {
                String servId = item.getServId();

                CentralDetailResDto wantedDtl = centralClient.getWelfareDetail(servId);

                // ✅ 카테고리 매핑
                List<LifeCycle> lifeCycles = parseLifeCycles(wantedDtl.getLifeArray());
                List<HouseholdType> householdTypes = parseHouseholdTypes(wantedDtl.getTrgterIndvdlArray());
                List<InterestTopic> interestTopics = parseInterestTopics(wantedDtl.getIntrsThemaArray());

                Category category = categoryService.findOrCreateCategory(lifeCycles, householdTypes, interestTopics);

                Welfare welfare = welfareRepository.findByServId(servId).orElse(null);

                if (welfare == null) {
                    Welfare newWelfare = Welfare.builder()
                            .servId(servId)
                            .title(wantedDtl.getServNm())
                            .content(wantedDtl.getWlfareInfoOutlCn())
                            .servLink(item.getServDtlLink())
                            .ctpvNm("지역정보없음")
                            .sggNm("지역정보없음")
                            .imageUrl(null)
                            .eligibleUser(wantedDtl.getTgtrDtlCn())
                            .submitDocument(wantedDtl.getAlwServCn())
                            .startDate(null)
                            .endDate(null)
                            .sourceType(SourceType.CENTRAL)
                            .category(category)
                            .build();
                    welfareRepository.save(newWelfare);
                } else {
                    boolean isUpdated = false;

                    if (!welfare.getContent().equals(wantedDtl.getWlfareInfoOutlCn()) ||
                            !welfare.getServLink().equals(item.getServDtlLink()) ||
                            !welfare.getEligibleUser().equals(wantedDtl.getTgtrDtlCn()) ||
                            !welfare.getSubmitDocument().equals(wantedDtl.getAlwServCn())) {

                        welfare.update(
                                wantedDtl.getWlfareInfoOutlCn(),
                                item.getServDtlLink(),
                                wantedDtl.getTgtrDtlCn(),
                                wantedDtl.getAlwServCn()
                        );
                        isUpdated = true;
                    }

                    // ✅ 카테고리가 변경되었을 수도 있음
                    if (!welfare.getCategory().getId().equals(category.getId())) {
                        welfare.updateCategory(category);
                        isUpdated = true;
                    }

                    // ✅ 지역정보가 비어있으면 기본값 세팅
                    if (welfare.getCtpvNm() == null || welfare.getSggNm() == null) {
                        welfare.updateRegion("지역정보없음", "지역정보없음");
                        isUpdated = true;
                    }

                    if (isUpdated) {
                        welfareRepository.save(welfare);
                    }
                }
            }

            log.info("[복지 동기화] {}페이지 동기화 완료", page);
            page++;
        }

        log.info("[복지 동기화] 중앙부처 전체 동기화 완료");
    }
}
