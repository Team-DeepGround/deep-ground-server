package com.samsamhajo.deepground.feed.feedreply.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;

public class FeedReplyException extends BaseException {
    public FeedReplyException(FeedReplyErrorCode errorCode) {
        super(errorCode);
    }
} 