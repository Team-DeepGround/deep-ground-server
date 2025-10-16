package com.samsamhajo.deepground.notification.success;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationSuccessCode implements SuccessCode {
    NOTIFICATION_RETRIEVED(HttpStatus.OK, "알림 목록을 성공적으로 조회했습니다."),
    NOTIFICATION_READ(HttpStatus.OK, "알림을 읽었습니다."),
    NOTIFICATION_UNREAD_COUNT_RETRIEVED(HttpStatus.OK, "안 읽은 알림 개수를 성공적으로 조회했습니다."),
    NOTIFICATION_SUCCESS_DELETED(HttpStatus.OK, "알림이 성공적으로 제거되었습니다.");

    private final HttpStatus status;
    private final String message;
}
