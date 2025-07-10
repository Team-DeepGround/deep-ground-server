package com.samsamhajo.deepground.techStack.success;

import com.samsamhajo.deepground.global.success.SuccessCode;
import org.springframework.http.HttpStatus;

public enum TechStackSuccessCode implements SuccessCode {
    READ_SUCCESS(HttpStatus.OK, "기술스택 전체 조회 성공");

    private final HttpStatus status;
    private final String message;

    TechStackSuccessCode(HttpStatus status, String message) {
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