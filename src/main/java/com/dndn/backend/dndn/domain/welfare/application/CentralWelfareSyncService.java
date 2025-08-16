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
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
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

                String detailInfo = Optional.ofNullable(wantedDtl.getBasfrmList())
                        .flatMap(list -> list.stream()
                                .map(CentralDetailResDto.ServDetail::getServSeDetailLink)
                                .filter(Objects::nonNull)
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .findFirst())
                        .orElse(null);

                String servLink = Optional.ofNullable(wantedDtl.getInqplHmpgReldList())
                        .flatMap(list -> list.stream()
                                .map(CentralDetailResDto.ServDetail::getServSeDetailLink)
                                .filter(Objects::nonNull)
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .findFirst())
                        .orElse(null);

                String org = Optional.ofNullable(wantedDtl.getInqplCtadrList())
                        .flatMap(list -> list.stream()
                                .map(CentralDetailResDto.ServDetail::getServSeDetailNm)
                                .filter(Objects::nonNull)
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .findFirst())
                        .orElseGet(wantedDtl::getRprsCtadr);

                if (welfare == null) {
                    Welfare newWelfare = Welfare.builder()
                            .servId(servId)
                            .title(wantedDtl.getServNm())
                            .summary(wantedDtl.getWlfareInfoOutlCn())
                            .content(wantedDtl.getAlwServCn())
                            .servLink(servLink)
                            .ctpvNm("지역정보없음")
                            .sggNm("지역정보없음")
                            .department(wantedDtl.getJurMnofNm())
                            .org(org)
                            .imageUrl(null)
                            .eligibleUser(wantedDtl.getTgtrDtlCn())
                            .detailInfo(detailInfo)
                            .startDate(null)
                            .endDate(null)
                            .sourceType(SourceType.CENTRAL)
                            .category(category)
                            .build();
                    welfareRepository.save(newWelfare);
                } else {
                    String newSummary      = wantedDtl.getWlfareInfoOutlCn();
                    String newContent      = wantedDtl.getAlwServCn();
                    String newServLink     = servLink;
                    String newDetailInfo   = detailInfo;
                    String newDepartment   = wantedDtl.getJurMnofNm();
                    String newEligibleUser = wantedDtl.getTgtrDtlCn();
                    String newOrg = org;

                    boolean needsUpdate =
                            !Objects.equals(welfare.getSummary(),      newSummary)      ||
                                    !Objects.equals(welfare.getContent(),      newContent)      ||
                                    !Objects.equals(welfare.getServLink(),     newServLink)     ||
                                    !Objects.equals(welfare.getDepartment(),   newDepartment)   ||
                                    !Objects.equals(welfare.getOrg(),          newOrg)          ||
                                    !Objects.equals(welfare.getEligibleUser(), newEligibleUser) ||
                                    !Objects.equals(welfare.getDetailInfo(),   newDetailInfo);

                    if (needsUpdate) {
                        welfare.update(
                                newSummary,
                                newContent,
                                newServLink,
                                newDepartment,
                                newOrg,
                                newEligibleUser,
                                newDetailInfo
                        );
                    }

                    // ✅ 카테고리가 변경되었을 수도 있음
                    if (!welfare.getCategory().getId().equals(category.getId())) {
                        welfare.updateCategory(category);
                        needsUpdate = true;
                    }

                    // ✅ 지역정보가 비어있으면 기본값 세팅
                    if (welfare.getCtpvNm() == null || welfare.getSggNm() == null) {
                        welfare.updateRegion("지역정보없음", "지역정보없음");
                        needsUpdate = true;
                    }

                    if (needsUpdate) {
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
