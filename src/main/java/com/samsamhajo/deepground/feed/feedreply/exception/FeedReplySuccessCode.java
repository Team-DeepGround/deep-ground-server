package com.samsamhajo.deepground.feed.feedreply.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedReplySuccessCode implements SuccessCode {
    FEED_REPLY_CREATED(HttpStatus.CREATED, "답글이 생성되었습니다."),
    FEED_REPLY_UPDATED(HttpStatus.OK, "답글이 수정되었습니다."),
    FEED_REPLY_LIKED(HttpStatus.OK, "답글이 좋아요 되었습니다."),
    FEED_REPLY_DISLIKED(HttpStatus.OK, "답글 좋아요가 취소되었습니다.");

    private final HttpStatus status;
    private final String message;
} 