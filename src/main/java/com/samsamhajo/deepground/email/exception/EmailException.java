package com.samsamhajo.deepground.email.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;
import com.samsamhajo.deepground.global.error.core.ErrorCode;

public class EmailException extends BaseException {
    public EmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
