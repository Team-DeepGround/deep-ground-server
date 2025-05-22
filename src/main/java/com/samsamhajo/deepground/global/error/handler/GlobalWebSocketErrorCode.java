package com.samsamhajo.deepground.global.error.handler;

import com.samsamhajo.deepground.global.error.core.WebSocketErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalWebSocketErrorCode implements WebSocketErrorCode {
    INTERNAL_SERVER_ERROR(4000, "서버 내부 오류가 발생했습니다."),
    ;

    private final int status;
    private final String message;
}
