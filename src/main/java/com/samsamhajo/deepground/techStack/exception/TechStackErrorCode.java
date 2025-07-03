package com.samsamhajo.deepground.techStack.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum TechStackErrorCode implements ErrorCode {

    TECH_STACK_NOT_FOUND(HttpStatus.NOT_FOUND, "기술스택이 존재하지 않습니다."),
    ;


    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return "[TECHSTACK ERROR]" + message;
    }
}
