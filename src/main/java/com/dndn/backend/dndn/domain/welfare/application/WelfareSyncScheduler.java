package com.dndn.backend.dndn.domain.welfare.application;

import com.dndn.backend.dndn.domain.welfareOpenApi.central.client.CentralWelfareClient;
import com.dndn.backend.dndn.domain.welfareOpenApi.local.client.LocalWelfareClient;
import com.dndn.backend.dndn.domain.welfareOpenApi.local.dto.response.LocalDetailResDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WelfareSyncScheduler {

    private final CentralWelfareSyncService centralWelfareSyncService;
    private final LocalWelfareSyncService localWelfareSyncService;
    private final CentralWelfareClient centralClient;
    private final LocalWelfareClient localClient;

//    @PostConstruct
//    public void initSync() {
//        String rawXml = localClient.debugWelfareDetail("WLF00001760");
//        log.info("[Raw XML 출력]\n{}", rawXml);
//    }

    @EventListener(ApplicationReadyEvent.class)
    public void runOnceAfterStartup() {
        log.info("[복지 동기화] 앱 기동 후 1회 실행 시작");
        try {
//            centralWelfareSyncService.syncCentralWelfareData();
            localWelfareSyncService.syncLocalWelfareData();
        } catch (Exception e) {
            log.error("[복지 동기화] 실행 중 예외", e); // ← 여기서 원인 예외 전체가 보입니다.
        }
    }
//    @PostConstruct
//    public void initSync() {
//        log.info("[복지 동기화] 초기 실행 시작");
//        centralWelfareSyncService.syncCentralWelfareData();
////        localWelfareSyncService.syncLocalWelfareData();
//    }

//    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
//    public void scheduledSync() {
//        log.info("[복지 동기화] 스케줄 실행 시작");
//        centralWelfareSyncService.syncCentralWelfareData();
//        localWelfareSyncService.syncLocalWelfareData();
//    }


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

