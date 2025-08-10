package com.dndn.backend.dndn.domain.welfare.exception;

import com.dndn.backend.dndn.global.error.code.status.BaseErrorCode;
import com.dndn.backend.dndn.global.exception.GeneralException;

public class WelfareException extends GeneralException {
    public WelfareException(BaseErrorCode code) {
        super(code);
    }
}
