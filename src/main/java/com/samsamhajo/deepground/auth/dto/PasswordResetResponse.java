package com.samsamhajo.deepground.auth.dto;

import com.samsamhajo.deepground.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordResetResponse {
    private Long memberId;
    private String email;
    private boolean isSuccess;

    private PasswordResetResponse(Long memberId, String email, boolean isSuccess) {
        this.memberId = memberId;
        this.email = email;
        this.isSuccess = isSuccess;
    }

    public static PasswordResetResponse of(Member member) {
        return PasswordResetResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .isSuccess(true)
                .build();
    }
}
