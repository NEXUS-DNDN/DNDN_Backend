package com.dndn.backend.dndn.domain.welfare.domain.enums;

import lombok.Getter;

@Getter
public enum ReceiveStatus {
    NOT_RECEIVED,  // 수령 전
    RECEIVED       // 수령 완료
}
