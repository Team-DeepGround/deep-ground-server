package com.samsamhajo.deepground.feed.feed.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FeedErrorCode implements ErrorCode {
    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "피드를 찾을 수 없습니다."),
    INVALID_FEED_CONTENT(HttpStatus.BAD_REQUEST, "피드 내용이 유효하지 않습니다."),
    FEED_UPDATE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "피드 수정 권한이 없습니다."),
    FEED_DELETE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "피드 삭제 권한이 없습니다."),
    FEED_MEDIA_NOT_FOUND(HttpStatus.NOT_FOUND, "피드 미디어를 찾을 수 없습니다."),

    FEED_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 피드 및 좋아요를 찾을 수 없습니다."),
    FEED_LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 좋아요가 눌러져 있습니다."),

    ;

    private final HttpStatus status;
    private final String message;

    FeedErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}