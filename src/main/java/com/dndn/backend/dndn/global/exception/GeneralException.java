package com.dndn.backend.dndn.global.exception;

import com.dndn.backend.dndn.global.error.code.status.BaseErrorCode;
import com.dndn.backend.dndn.global.error.code.status.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;
    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
