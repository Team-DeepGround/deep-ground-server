package com.samsamhajo.deepground.email.success;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailSuccessCode implements SuccessCode {

    EMAIL_SENT(HttpStatus.OK, "인증 코드가 이메일로 전송되었습니다."),
    EMAIL_VERIFIED(HttpStatus.OK, "이메일 인증이 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return "[EMAIL] " + message;
    }
}
