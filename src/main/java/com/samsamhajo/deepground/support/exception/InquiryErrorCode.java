package com.samsamhajo.deepground.support.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum InquiryErrorCode implements ErrorCode {

    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "문의가 존재하지 않습니다."),
    ;
    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return "[INQUIRY ERROR] " + message;
    }
}
