package com.samsamhajo.deepground.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordResetEmailResponse {
    private final String email;
    private final boolean isSuccess;

    private PasswordResetEmailResponse(String email, boolean isSuccess) {
        this.email = email;
        this.isSuccess = isSuccess;
    }

    public static PasswordResetEmailResponse of(String email) {
        return PasswordResetEmailResponse.builder()
                .email(email)
                .isSuccess(true)
                .build();
    }
}
