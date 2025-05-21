package com.samsamhajo.deepground.calendar.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ScheduleSuccessCode implements SuccessCode {
    SCHEDULE_CREATED(HttpStatus.CREATED, "일정이 성공적으로 등록되었습니다."),
    SCHEDULE_UPDATED(HttpStatus.OK, "일정이 성공적으로 수정되었습니다.");

    private final HttpStatus status;
    private final String message;

    ScheduleSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
