package com.samsamhajo.deepground.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PasswordResetEmailResponse {
    private final String email;
    private final boolean isSuccess;

    public static PasswordResetEmailResponse of(String email) {
        return PasswordResetEmailResponse.builder()
                .email(email)
                .isSuccess(true)
                .build();
    }
}
