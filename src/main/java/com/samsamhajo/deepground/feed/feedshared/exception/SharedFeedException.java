package com.samsamhajo.deepground.feed.feedshared.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;

public class SharedFeedException extends BaseException {
    public SharedFeedException(SharedFeedErrorCode errorCode) {
        super(errorCode);
    }
} 