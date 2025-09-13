package com.samsamhajo.deepground.global.error.core;

public interface WebSocketErrorCode {

    int getStatus();

    String getMessage();

    default String getMessage(Object... args) {
        return String.format(this.getMessage(), args);
    }
}
