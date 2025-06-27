package com.samsamhajo.deepground.member.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProfileSuccessCode implements SuccessCode {

    PROFILE_SUCCESS_CODE(HttpStatus.OK, "프로필 정보가 성공적으로 업데이트되었습니다."),
    GET_SUCCESS_PROFILE(HttpStatus.OK,"사용자의 프로필 정보를 정상적으로 조회했습니다" );

    private final HttpStatus status;
    private final String message;

    ProfileSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
