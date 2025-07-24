package com.dndn.backend.dndn.global.error.code.status;

public interface BaseCode {

    String getCode();

    String getMessage();

    ReasonDTO getReasonHttpStatus();
}
