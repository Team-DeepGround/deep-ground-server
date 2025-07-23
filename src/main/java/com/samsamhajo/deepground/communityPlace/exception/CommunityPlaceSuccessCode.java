package com.samsamhajo.deepground.communityPlace.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommunityPlaceSuccessCode implements SuccessCode {

    REVIEW_CREATED(HttpStatus.CREATED, "리뷰가 정상적으로 생성되었습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
