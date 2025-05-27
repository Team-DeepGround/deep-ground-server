package com.samsamhajo.deepground.auth.service;

import com.samsamhajo.deepground.auth.dto.*;
import com.samsamhajo.deepground.auth.exception.AuthErrorCode;
import com.samsamhajo.deepground.auth.exception.AuthException;
import com.samsamhajo.deepground.email.repository.EmailVerificationRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

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
    void 회원가입_성공() {
        // given
        RegisterRequest request = new RegisterRequest(
                "new@example.com",
                "password123",
                "test02"
        );

        // when
        Long memberId = authService.register(request);

        // then
        Member savedMember = memberRepository.findById(memberId)
                .orElseThrow();

        assertThat(savedMember.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedMember.getNickname()).isEqualTo(request.getNickname());
        assertThat(savedMember.isVerified()).isFalse();
    }
    @Test
    void 이메일_중복_검사_성공() {
        // when & then
       assertDoesNotThrow(() ->
               authService.checkEmailDuplicate("test123@example.com"));
    }

    @Test
    void 이메일_중복_검사_실패() {
        // when & then
        AuthException exception = assertThrows(AuthException.class, () ->
                authService.checkEmailDuplicate(member.getEmail()));

        assertEquals(AuthErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());
    }

    @Test
    void 닉네임_중복_검사_성공() {
        // when & then
        assertDoesNotThrow(() ->
                authService.checkNicknameDuplicate("new"));
    }

    @Test
    void 닉네임_중복_검사_실패() {
        // when & then
        AuthException exception = assertThrows(AuthException.class, () ->
                authService.checkNicknameDuplicate(member.getNickname()));

        assertEquals(AuthErrorCode.DUPLICATE_NICKNAME, exception.getErrorCode());
    }

    @Test
    void 중복된_이메일로_회원가입시_실패() {
        // given
        RegisterRequest request = new RegisterRequest(
                member.getEmail(),
                "password123",
                "test03"
        );

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> authService.register(request));

        assertEquals(AuthErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());
    }

    @Test
    void 중복된_닉네임으로_회원가입시_실패() {
        // given
        RegisterRequest request = new RegisterRequest(
                "test3@example.com",
                "password123",
                member.getNickname()
        );

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> authService.register(request));

        assertEquals(AuthErrorCode.DUPLICATE_NICKNAME, exception.getErrorCode());
    }

    @Test
    void 로그인_성공() {
        // given
        LoginRequest request = new LoginRequest(
                member.getEmail(),
                "password123"
        );

        // when
        LoginResponse response = authService.login(request);

        //then
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertEquals(member.getId(), response.getMemberId());
        assertEquals(member.getEmail(), response.getEmail());
        assertEquals(member.getNickname(), response.getNickname());
    }

    @Test
    void 존재하지_않는_이메일로_로그인_실패() {
        // given
        LoginRequest request = new LoginRequest(
                "notfound@example.com",
                "password123"
        );

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> authService.login(request));

        assertEquals(AuthErrorCode.INVALID_EMAIL, exception.getErrorCode());
    }

    @Test
    void 잘못된_비밀번호로_로그인_실패() {
        // given
        LoginRequest request = new LoginRequest(
                member.getEmail(),
                "wrongpassword"
        );

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> authService.login(request));

        assertEquals(AuthErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

    @Test
    void 비밀번호_재설정_이메일_전송_성공() {
        // given
        PasswordResetRequest request = new PasswordResetRequest(member.getEmail());

        // when
        PasswordResetEmailResponse response = authService.sendPasswordResetEmail(request);

        // then
        assertNotNull(response);
        assertEquals(member.getEmail(), response.getEmail());
        assertTrue(response.isSuccess());
    }

    @Test
    void 존재하지_않는_이메일로_비밀번호_재설정_요청_실패() {
        // given
        PasswordResetRequest request = new PasswordResetRequest("wrong@example.com");

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> authService.sendPasswordResetEmail(request));
        assertEquals(AuthErrorCode.INVALID_EMAIL, exception.getErrorCode());
    }

    @Test
    void 비밀번호_재설정_성공() {
        // given
        String code = "123456";
        String newPassword = "newPassword123";
        emailVerificationRepository.save(member.getEmail(), code, 300L);

        PasswordResetVerifyRequest request = new PasswordResetVerifyRequest(
                member.getEmail(),
                code,
                newPassword
        );

        // when
        PasswordResetResponse response = authService.resetPassword(request);

        // then
        assertNotNull(response);
        assertEquals(member.getId(), response.getMemberId());
        assertEquals(member.getEmail(), response.getEmail());
        assertTrue(response.isSuccess());

        // 비밀번호 변경 확인
        Member updatedMember = memberRepository.findByEmail(member.getEmail()).orElseThrow();
        assertTrue(passwordEncoder.matches(newPassword, updatedMember.getPassword()));

        // 인증 코드 삭제 확인
        assertNull(emailVerificationRepository.getCode(member.getEmail()));
    }

    @Test
    void 잘못된_인증코드로_비밀번호_재설정_실패() {
        // given
        String code = "123456";
        String wrongCode = "wrong123";
        String newPassword = "newPassword123";
        emailVerificationRepository.save(member.getEmail(), code, 300L);

        PasswordResetVerifyRequest request = new PasswordResetVerifyRequest(
                member.getEmail(),
                wrongCode,
                newPassword
        );

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> authService.resetPassword(request));
        assertEquals(AuthErrorCode.INVALID_VERIFICATION_CODE, exception.getErrorCode());

        // 기존 비밀번호가 유지되는지 확인
        Member unchangedMember = memberRepository.findByEmail(member.getEmail()).orElseThrow();
        assertTrue(passwordEncoder.matches("password123", unchangedMember.getPassword()));
    }

    @Test
    void 만료된_인증코드로_비밀번호_재설정_실패() {
        // given
        String code = "123456";
        String newPassword = "newPassword123";
        PasswordResetVerifyRequest request = new PasswordResetVerifyRequest(
                member.getEmail(),
                code,
                newPassword
        );

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> authService.resetPassword(request));
        assertEquals(AuthErrorCode.VERIFICATION_CODE_EXPIRED, exception.getErrorCode());

        Member unchangedMember = memberRepository.findByEmail(member.getEmail()).orElseThrow();
        assertTrue(passwordEncoder.matches("password123", unchangedMember.getPassword()));
    }
}
