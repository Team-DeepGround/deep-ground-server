package com.samsamhajo.deepground.notification.success;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationSuccessCode implements SuccessCode {
    NOTIFICATION_READ(HttpStatus.OK, "알림을 읽었습니다.");

    private final HttpStatus status;
    private final String message;
}
