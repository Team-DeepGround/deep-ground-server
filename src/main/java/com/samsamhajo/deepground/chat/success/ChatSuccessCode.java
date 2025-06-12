package com.samsamhajo.deepground.chat.success;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatSuccessCode implements SuccessCode {
    CHAT_MEDIA_UPLOADED(HttpStatus.CREATED, "채팅 미디어가 성공적으로 업로드되었습니다."),
    CHATROOM_MEMBER_INFO_RETRIEVED(HttpStatus.OK, "채팅방 멤버 정보를 성공적으로 조회했습니다."),
    CHAT_MESSAGE_RETRIEVED(HttpStatus.OK, "채팅 메시지를 성공적으로 조회했습니다.");

    private final HttpStatus status;
    private final String message;
}
