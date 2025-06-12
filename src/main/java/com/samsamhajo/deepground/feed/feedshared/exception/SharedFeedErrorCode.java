package com.samsamhajo.deepground.feed.feedshared.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SharedFeedErrorCode implements ErrorCode {
    SHARED_FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "공유된 피드를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
} 