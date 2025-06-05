package com.samsamhajo.deepground.chat.success;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatMediaSuccessCode implements SuccessCode {
    CHAT_MEDIA_UPLOADED(HttpStatus.CREATED, "채팅 미디어가 성공적으로 업로드되었습니다.");

    private final HttpStatus status;
    private final String message;
}
