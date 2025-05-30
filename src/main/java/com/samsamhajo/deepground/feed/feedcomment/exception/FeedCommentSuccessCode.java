package com.samsamhajo.deepground.feed.feedcomment.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedCommentSuccessCode implements SuccessCode {
    FEED_COMMENT_CREATED(HttpStatus.CREATED, "댓글이 생성되었습니다.")

    ;

    private final HttpStatus status;
    private final String message;
} 