package com.samsamhajo.deepground.member.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberSuccessCode implements SuccessCode {

    ONLINE_SUCCESS_CODE(HttpStatus.OK, "온라인/오프라인 상태를 성공적으로 조회했습니다."),
    GET_MEMBER_STUDY_SUCCESS(HttpStatus.OK, "해당 멤버의 스터디를 조회했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
