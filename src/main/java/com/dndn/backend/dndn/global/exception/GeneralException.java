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

    // ✅ cause도 받는 생성자 추가
    public GeneralException(BaseErrorCode code, Throwable cause) {
        super(code.getReasonHttpStatus().getMessage(), cause);
        this.code = code;
    }
}
