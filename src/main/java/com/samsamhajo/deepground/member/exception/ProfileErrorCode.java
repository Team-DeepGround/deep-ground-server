package com.samsamhajo.deepground.member.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ProfileErrorCode implements ErrorCode {

    INVALID_PROFILE_ID(HttpStatus.BAD_REQUEST, "회원의 프로필이 존재하지 않습니다."),
    ALREADY_EXISTS_PROFILE(HttpStatus.CONFLICT, "이미 프로필이 존재합니다."),
    ;


    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return "[PROFILE ERROR]" + message;
    }
}
