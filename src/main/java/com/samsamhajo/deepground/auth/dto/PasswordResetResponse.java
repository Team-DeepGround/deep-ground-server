package com.samsamhajo.deepground.auth.dto;

import com.samsamhajo.deepground.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PasswordResetResponse {
    private Long memberId;
    private String email;
    private boolean isSuccess;

    public static PasswordResetResponse of(Member member) {
        return PasswordResetResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .isSuccess(true)
                .build();
    }
}
