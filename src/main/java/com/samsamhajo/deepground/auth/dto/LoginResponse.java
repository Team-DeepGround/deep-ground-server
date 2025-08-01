package com.samsamhajo.deepground.auth.dto;

import com.samsamhajo.deepground.member.entity.Role;
import lombok.Getter;

@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String email;
    private String nickname;
    private Role role;

    public LoginResponse(String accessToken, String refreshToken, Long memberId, String email, String nickname, Role role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }
}
