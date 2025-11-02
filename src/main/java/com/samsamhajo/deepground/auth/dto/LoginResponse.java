package com.samsamhajo.deepground.auth.dto;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.entity.Role;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String accessToken;
    private final Long memberId;
    private final String email;
    private final String nickname;
    private final Role role;
    private final String provider;

    public LoginResponse(String accessToken, Long memberId, String email, String nickname, Role role, String provider) {
        this.accessToken = accessToken;
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.provider = provider;
    }

    public static LoginResponse from(Member member, String accessToken) {
        String provider = "LOCAL";
        if (member.getGoogleId() != null) provider = "GOOGLE";
        else if (member.getNaverId() != null) provider = "NAVER";

        return new LoginResponse(
                accessToken,
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole(),
                provider
        );
    }
}
