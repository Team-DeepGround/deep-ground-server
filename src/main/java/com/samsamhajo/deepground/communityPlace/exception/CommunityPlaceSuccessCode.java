package com.samsamhajo.deepground.communityPlace.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommunityPlaceSuccessCode implements SuccessCode {

    COMMUNITYPLACE_SUCCESS_SELECT(HttpStatus.OK, "스터디 장소가 성공적으로 조회됐습니다");

    private final HttpStatus status;
    private final String message;

    CommunityPlaceSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
