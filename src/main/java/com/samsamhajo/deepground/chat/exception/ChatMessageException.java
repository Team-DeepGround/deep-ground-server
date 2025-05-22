package com.samsamhajo.deepground.chat.exception;

import com.samsamhajo.deepground.global.error.core.WebSocketErrorCode;
import com.samsamhajo.deepground.global.error.core.WebSocketException;

public class ChatMessageException extends WebSocketException {

    public ChatMessageException(WebSocketErrorCode errorCode) {
        super(errorCode);
    }
}
