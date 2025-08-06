package com.dndn.backend.dndn.domain.welfare.application;

import com.dndn.backend.dndn.domain.welfare.domain.enums.ReceiveStatus;
import com.dndn.backend.dndn.domain.welfare.domain.enums.RequestStatus;
import com.dndn.backend.dndn.domain.welfare.domain.enums.SourceType;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import com.dndn.backend.dndn.domain.welfare.domain.repository.CategoryRepository;
import com.dndn.backend.dndn.domain.welfare.domain.repository.WelfareRepository;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.client.CentralWelfareClient;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralDetailResDto;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.dto.response.CentralListResDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WelfareSyncService {

    private final WelfareRepository welfareRepository;
    private final CategoryRepository categoryRepository;

    private final CentralWelfareClient centralClient;

    @PostConstruct
    public void initSync() {
        try {
            log.info("[복지 동기화] 최초 1회 중앙부처 복지 동기화 시작");
            syncCentralWelfareData();
        } catch (Exception e) {
            log.error("복지 동기화 실패", e);
        }
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul") // 매일 새벽 3시
    public void syncCentralWelfareData() {
        int page = 1;
        int numOfRows = 100;

        while (true) {
            CentralListResDto centralListResDto = centralClient.getWelfareList(page, numOfRows);
            List<CentralListResDto.ServiceItem> serviceItems = centralListResDto.getWantedList().getServList();
            if (serviceItems == null || serviceItems.isEmpty()) break;

            for (CentralListResDto.ServiceItem item : serviceItems) {
                String servId = item.getServId();
                String title = item.getServNm();

                // 상세 정보 요청
                CentralDetailResDto centralDetailResDto = centralClient.getWelfareDetail(servId);
                CentralDetailResDto.WantedDtl wantedDtl = centralDetailResDto.getWantedDtl();

                Welfare welfare = welfareRepository.findByServId(servId).orElse(null);

                if (welfare == null) {
                    Welfare newWelfare = Welfare.builder()
                            .servId(servId)
                            .title(title)
                            .content(wantedDtl.getWlfareInfoOutlCn())
                            .servLink(item.getServDtlLink())
                            .imageUrl(null)
                            .eligibleUser(wantedDtl.getTgtrDtlCn())
                            .submitDocument(wantedDtl.getAlwServCn())
                            .startDate(null) // 시작일 정보 없음
                            .endDate(null)   // 마감일 정보 없음
                            .requestStatus(RequestStatus.NOT_REQUESTED)
                            .receiveStatus(ReceiveStatus.NOT_RECEIVED)
                            .sourceType(SourceType.CENTRAL)
                            .build();
                    welfareRepository.save(newWelfare);
                } else {
                    // 내용이 변경된 경우에만 update() 호출
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
