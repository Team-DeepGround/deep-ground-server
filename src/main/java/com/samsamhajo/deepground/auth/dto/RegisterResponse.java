package com.samsamhajo.deepground.auth.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RegisterResponse {

    private final UUID publicId;

    private final boolean needVerification;

    public RegisterResponse(UUID publicId, boolean needVerification) {
        this.publicId = publicId;
        this.needVerification = needVerification;
    }
}
