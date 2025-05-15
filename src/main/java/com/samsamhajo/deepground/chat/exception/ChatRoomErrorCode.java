package com.samsamhajo.deepground.chat.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ChatRoomErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "채팅방 멤버를 찾을 수 없습니다.");

    public static final String PREFIX = "[CHATROOM ERROR] ";

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return PREFIX + message;
    }
}
