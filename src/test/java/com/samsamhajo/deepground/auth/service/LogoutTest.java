package com.samsamhajo.deepground.auth.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.auth.dto.LoginRequest;
import com.samsamhajo.deepground.auth.dto.LoginResponse;
import com.samsamhajo.deepground.auth.repository.RefreshTokenRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class LogoutTest extends IntegrationTestSupport {
    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member member;

    @BeforeEach
    void setup() {
        member = Member.createLocalMember(
                "test@example.com",
                passwordEncoder.encode("password123"),
                "test01"
        );
        memberRepository.save(member);
    }

    @Test
    void 로그아웃_시_Refresh_Token_삭제() {
        // given
        refreshTokenRepository.save(member.getId(), "refresh_token", 3600L);

        // Redis에 Refresh Token이 저장되어 있는지 확인
        assertNotNull(refreshTokenRepository.findByMemberId(member.getId()));

        // when
        authService.logout(member.getId());

        // then
        assertNull(refreshTokenRepository.findByMemberId(member.getId()));
    }

    @Test
    void 로그아웃_후_재로그인하면_새로운_Refresh_Token_발급() {
        // given
        refreshTokenRepository.save(member.getId(), "initial_token", 3600L);

        // when
        authService.logout(member.getId());
        LoginRequest loginRequest = new LoginRequest(member.getEmail(), "password123");
        LoginResponse loginResponse = authService.login(loginRequest);

        // then
        String newRefreshToken = refreshTokenRepository.findByMemberId(member.getId());
        assertNotNull(newRefreshToken);
        assertNotEquals("initial_token", newRefreshToken);
        assertEquals(loginResponse.getRefreshToken(), newRefreshToken);
    }
}
