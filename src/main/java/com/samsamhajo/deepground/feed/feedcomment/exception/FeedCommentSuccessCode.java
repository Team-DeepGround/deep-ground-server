package com.samsamhajo.deepground.feed.feedcomment.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedCommentSuccessCode implements SuccessCode {
    FEED_COMMENT_CREATED(HttpStatus.CREATED, "댓글이 생성되었습니다."),
    FEED_COMMENT_UPDATED(HttpStatus.OK, "댓글이 수정되었습니다."),
    FEED_COMMENT_LIKED(HttpStatus.OK, "댓글이 좋아요 되었습니다."),
    FEED_COMMENT_DISLIKED(HttpStatus.OK, "댓글 좋아요가 취소되었습니다."),
    FEED_COMMENT_DELETED(HttpStatus.OK, "댓글이 삭제되었습니다."),
    FEED_COMMENT_LIST_FETCHED(HttpStatus.OK, "댓글 목록이 조회되었습니다."),

    ;

    private final HttpStatus status;
    private final String message;
} 