package com.samsamhajo.deepground.feed.feedreply.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedReplyErrorCode implements ErrorCode {
    INVALID_FEED_REPLY_CONTENT(HttpStatus.BAD_REQUEST, "답글 내용이 유효하지 않습니다."),
    FEED_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "답글을 찾을 수 없습니다."),

    FEED_REPLY_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "답글 좋아요를 찾을 수 없습니다."),
    FEED_REPLY_LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 답글입니다."),
    FEED_LIKE_MINUS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "답글 좋아요는 0보다 작을 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String message;
} 