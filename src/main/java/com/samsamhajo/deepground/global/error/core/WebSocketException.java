package com.samsamhajo.deepground.global.error.core;

import lombok.Getter;

@Getter
public abstract class WebSocketException extends RuntimeException {

    private final WebSocketErrorCode errorCode;

    public WebSocketException(WebSocketErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public WebSocketException(WebSocketErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
