package com.samsamhajo.deepground.address.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AddressErrorCode implements ErrorCode {
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주소입니다."),
    INVALID_ADDRESS_INCLUDED(HttpStatus.BAD_REQUEST, "유효하지 않은 주소가 포함되어 있습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return "[ADDRESS ERROR] " + message;
    }
}
