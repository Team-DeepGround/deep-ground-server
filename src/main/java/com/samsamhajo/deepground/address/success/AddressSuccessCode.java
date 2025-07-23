package com.samsamhajo.deepground.address.success;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AddressSuccessCode implements SuccessCode {
    GET_CITIES_SUCCESS(HttpStatus.OK, "시 목록 조회 성공"),
    GET_GUS_SUCCESS(HttpStatus.OK, "구 목록 조회 성공"),
    GET_DONGS_SUCCESS(HttpStatus.OK, "동 목록 조회 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return "[ADDRESS] " + message;
    }
}
