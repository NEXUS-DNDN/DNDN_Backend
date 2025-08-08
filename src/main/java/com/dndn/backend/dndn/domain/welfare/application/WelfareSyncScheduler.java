package com.dndn.backend.dndn.domain.welfare.application;

import com.dndn.backend.dndn.domain.category.domain.repository.CategoryRepository;
import com.dndn.backend.dndn.domain.welfare.domain.repository.WelfareRepository;
import com.dndn.backend.dndn.domain.welfareOpenApi.central.client.CentralWelfareClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WelfareSyncScheduler {

    private final CentralWelfareSyncService centralWelfareSyncService;
    private final LocalWelfareSyncService localWelfareSyncService;

    /*@PostConstruct
    public void initSync() {
        String rawXml = centralClient.debugWelfareListXml(1, 10);
        log.info("[Raw XML 출력]\n{}", rawXml);
    }*/

    @PostConstruct
    public void initSync() {
        log.info("[복지 동기화] 초기 실행 시작");
        centralWelfareSyncService.syncCentralWelfareData();
        localWelfareSyncService.syncLocalWelfareData();
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void scheduledSync() {
        log.info("[복지 동기화] 스케줄 실행 시작");
        centralWelfareSyncService.syncCentralWelfareData();
        localWelfareSyncService.syncLocalWelfareData();
    }


    /*@PostConstruct
    public void initSync() {
        try {
            log.info("[복지 동기화] 최초 1회 중앙부처 복지 동기화 시작");
            syncCentralWelfareData();
        } catch (Exception e) {
            log.error("복지 동기화 실패", e);
        }
    }*/

}

