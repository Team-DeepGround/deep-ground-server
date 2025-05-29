package com.samsamhajo.deepground.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PasswordResetVerifyRequest {

    @NotBlank(message = "email을 입력하세요.")
    @Email(message = "올바른 email을 입력하세요.")
    private String email;

    @NotBlank(message = "인증 코드를 입력하세요.")
    private String code;

    @NotBlank(message = "새로운 비밀번호를 입력하세요.")
    private String newPassword;
}
