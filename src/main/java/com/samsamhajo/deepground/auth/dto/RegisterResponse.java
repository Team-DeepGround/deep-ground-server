package com.samsamhajo.deepground.auth.dto;

import lombok.Getter;

@Getter
public class RegisterResponse {

    private final Long memberId;

    private final boolean needVerification;

    public RegisterResponse(Long memberId, boolean needVerification) {
        this.memberId = memberId;
        this.needVerification = needVerification;
    }
}
