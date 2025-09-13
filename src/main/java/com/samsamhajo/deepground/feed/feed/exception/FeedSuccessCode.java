package com.samsamhajo.deepground.feed.feed.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FeedSuccessCode implements SuccessCode {
    FEED_CREATED(HttpStatus.CREATED, "피드가 성공적으로 생성되었습니다."),
    FEEDS_RETRIEVED(HttpStatus.OK, "피드 목록이 성공적으로 조회되었습니다."),
    FEED_UPDATED(HttpStatus.OK, "피드가 성공적으로 수정되었습니다."),
    FEED_DELETED(HttpStatus.OK, "피드가 성공적으로 삭제되었습니다."),
    FEED_MEDIA_UPDATED(HttpStatus.OK, "피드 미디어가 성공적으로 수정되었습니다."),
    FEED_LIKED(HttpStatus.OK, "피드가 성공적으로 좋아요 되었습니다."),
    FEED_UNLIKED(HttpStatus.OK, "피드 좋아요가 취소되었습니다."),
    FEED_LIST_FETCHED(HttpStatus.OK, "피드 목록이 성공적으로 조회되었습니다."),
    FEED_FETCHED(HttpStatus.OK, "피드가 성공적으로 조회되었습니다."),

    ;

    private final HttpStatus status;
    private final String message;

    FeedSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}