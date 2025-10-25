package com.samsamhajo.deepground.support.success;

import com.samsamhajo.deepground.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

public enum InquirySuccessCode implements SuccessCode {
    CREATE_SUCCESS(HttpStatus.CREATED, "문의에 성공했습니다."),
    GET_INQUIRY(HttpStatus.OK, "문의 조회에 성공했습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    InquirySuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
