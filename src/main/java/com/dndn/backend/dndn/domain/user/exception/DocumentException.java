package com.dndn.backend.dndn.domain.user.exception;

import com.dndn.backend.dndn.global.error.code.status.BaseErrorCode;
import com.dndn.backend.dndn.global.exception.GeneralException;

public class DocumentException extends GeneralException {
    public DocumentException(BaseErrorCode code) {
        super(code);
    }
}
