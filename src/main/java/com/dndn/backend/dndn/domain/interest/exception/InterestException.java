package com.dndn.backend.dndn.domain.interest.exception;

import com.dndn.backend.dndn.global.error.code.status.BaseErrorCode;
import com.dndn.backend.dndn.global.exception.GeneralException;

public class InterestException extends GeneralException {
    public InterestException(BaseErrorCode code) {
        super(code);
    }
}
