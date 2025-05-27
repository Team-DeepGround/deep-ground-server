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
public class PasswordResetRequest {

    @NotBlank(message = "email을 입력하세요.")
    @Email(message = "올바른 email을 입력하세요.")
    private String email;
}
