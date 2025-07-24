package com.dndn.backend.dndn.domain.model.exception;

import com.dndn.backend.dndn.global.error.code.status.BaseErrorCode;
import com.dndn.backend.dndn.global.exception.GeneralException;

public class TestException extends GeneralException {
    public TestException(BaseErrorCode code) {
        super(code);
    }
}
