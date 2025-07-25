package com.samsamhajo.deepground.external.kakao.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;

public class ExternalApiException extends BaseException {
    public ExternalApiException(ExternalApiErrorCode errorCode) {
        super(errorCode);
    }
    public ExternalApiException(ExternalApiErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 