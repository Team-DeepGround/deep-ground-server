package com.samsamhajo.deepground.studyGroup.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum StudyGroupErrorCode implements ErrorCode {

    BLANK_LOCATION(HttpStatus.BAD_REQUEST, "시, 구, 동 모두 입력해주세요");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return "[STUDY GROUP ERROR] " + message;
    }
}
