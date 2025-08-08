package com.dndn.backend.dndn.domain.welfare.domain.enums;

import lombok.Getter;

@Getter
public enum RequestStatus {
    NOT_REQUESTED,   // 신청 전
    IN_PROGRESS,     // 신청 진행 중 (신청하러가기 클릭함)
    COMPLETED        // 신청 완료
}
