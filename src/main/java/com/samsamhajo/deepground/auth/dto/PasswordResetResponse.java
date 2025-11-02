package com.samsamhajo.deepground.auth.dto;

import com.samsamhajo.deepground.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PasswordResetResponse {
    private UUID publicId;
    private String email;
    private boolean isSuccess;

    private PasswordResetResponse(UUID publicId, String email, boolean isSuccess) {
        this.publicId = publicId;
        this.email = email;
        this.isSuccess = isSuccess;
    }

    public static PasswordResetResponse of(Member member) {
        return PasswordResetResponse.builder()
                .publicId(member.getPublicId())
                .email(member.getEmail())
                .isSuccess(true)
                .build();
    }
}
