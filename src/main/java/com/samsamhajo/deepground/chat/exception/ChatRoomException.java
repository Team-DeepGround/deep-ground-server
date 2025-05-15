package com.samsamhajo.deepground.chat.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;
import com.samsamhajo.deepground.global.error.core.ErrorCode;

public class ChatRoomException extends BaseException {

    public ChatRoomException(ErrorCode errorCode) {
        super(errorCode);
    }
}
