package com.samsamhajo.deepground.notification.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;
import com.samsamhajo.deepground.global.error.core.ErrorCode;

public class NotificationException extends BaseException {

    public NotificationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
