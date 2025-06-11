package com.samsamhajo.deepground.feed.feedshared.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SharedFeedSuccessCode implements SuccessCode {
    FEED_SHARED(HttpStatus.CREATED, "피드가 공유 되었습니다."),

    ;

    private final HttpStatus status;
    private final String message;
} 