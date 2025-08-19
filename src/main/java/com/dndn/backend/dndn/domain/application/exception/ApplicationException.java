package com.dndn.backend.dndn.domain.application.exception;

import com.dndn.backend.dndn.global.error.code.status.BaseErrorCode;
import com.dndn.backend.dndn.global.exception.GeneralException;

public class ApplicationException extends GeneralException {
    public ApplicationException(BaseErrorCode code) {
        super(code);
    }
}
