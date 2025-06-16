package com.samsamhajo.deepground.chat.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;
import com.samsamhajo.deepground.global.error.core.ErrorCode;

public class ChatException extends BaseException {

    public ChatException(ErrorCode errorCode) {
        super(errorCode);
    }
}
