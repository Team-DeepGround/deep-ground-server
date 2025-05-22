package com.samsamhajo.deepground.calendar.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleSuccessCode implements SuccessCode {
    SCHEDULE_CREATED(HttpStatus.CREATED, "일정이 성공적으로 등록되었습니다."),
    SCHEDULE_UPDATED(HttpStatus.OK, "일정이 성공적으로 수정되었습니다."),
    SCHEDULE_FOUND(HttpStatus.OK, "스케줄 조회 성공");

    private final HttpStatus status;
    private final String message;

}
