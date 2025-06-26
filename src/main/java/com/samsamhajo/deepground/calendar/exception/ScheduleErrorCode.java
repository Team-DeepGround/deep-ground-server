package com.samsamhajo.deepground.calendar.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {
    DUPLICATE_SCHEDULE(HttpStatus.CONFLICT, "이미 같은 시간대에 등록된 일정이 있습니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "종료 시간이 시작 시간보다 늦을 수 없습니다."),
    MISSING_REQUIRED_FIELDS(HttpStatus.BAD_REQUEST, "필수 입력 값이 누락되었습니다."),
    STUDY_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "스터디 그룹을 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 일정을 찾을 수 없습니다."),
    MISMATCHED_GROUP(HttpStatus.FORBIDDEN, "해당 스케줄은 요청한 스터디 그룹에 속하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다."),
    CANNOT_SET_DETAILS_WITHOUT_ATTENDANCE(HttpStatus.BAD_REQUEST,"참석 여부를 먼저 선택해야 중요 여부와 메모를 입력할 수 있습니다."),
    UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "해당 스터디 그룹의 생성자가 아닙니다.");


    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
