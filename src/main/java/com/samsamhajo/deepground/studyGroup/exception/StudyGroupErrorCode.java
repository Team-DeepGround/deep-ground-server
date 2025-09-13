package com.samsamhajo.deepground.studyGroup.exception;


import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StudyGroupErrorCode implements ErrorCode {
    MEMBER_IS_LEADER(HttpStatus.BAD_REQUEST, "스터디 그룹장은 탈퇴할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return "[ERROR]" + message;
    }
}
