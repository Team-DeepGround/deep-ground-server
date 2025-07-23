package com.samsamhajo.deepground.communityPlace.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum CommunityPlaceErrorCode implements ErrorCode {
    COMMUNITYPLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스터디장소는 찾을 수 없습니다");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return "[COMMUNITYPLACE ERROR]" + message;
    }
}
