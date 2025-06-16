package com.samsamhajo.deepground.feed.feedcomment.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;

public class FeedCommentException extends BaseException {
    public FeedCommentException(FeedCommentErrorCode errorCode) {
        super(errorCode);
    }
} 