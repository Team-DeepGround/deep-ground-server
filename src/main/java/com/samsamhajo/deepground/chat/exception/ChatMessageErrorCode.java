package com.samsamhajo.deepground.chat.exception;

import com.samsamhajo.deepground.global.error.core.WebSocketErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatMessageErrorCode implements WebSocketErrorCode {
    INVALID_MESSAGE(4001, "유효하지 않은 메시지입니다."),
    MEDIA_NOT_FOUND(4002, "미디어를 찾을 수 없습니다."),
    CHATROOM_MEMBER_NOT_FOUND(4003, "채팅방 멤버를 찾을 수 없습니다."),
    INVALID_MESSAGE_TIME(4004, "유효하지 않은 메시지 시간입니다.");

    private final int status;
    private final String message;
}
