package com.samsamhajo.deepground.auth.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.auth.dto.PasswordResetEmailResponse;
import com.samsamhajo.deepground.auth.dto.PasswordResetRequest;
import com.samsamhajo.deepground.auth.dto.PasswordResetResponse;
import com.samsamhajo.deepground.auth.dto.PasswordResetVerifyRequest;
import com.samsamhajo.deepground.auth.exception.AuthErrorCode;
import com.samsamhajo.deepground.auth.exception.AuthException;
import com.samsamhajo.deepground.email.repository.EmailVerificationRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class PasswordResetTest extends IntegrationTestSupport {

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
        member.verify();
        memberRepository.save(member);
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
