package com.samsamhajo.deepground.auth.service;

import com.samsamhajo.deepground.auth.dto.*;
import com.samsamhajo.deepground.auth.exception.AuthErrorCode;
import com.samsamhajo.deepground.auth.exception.AuthException;
import com.samsamhajo.deepground.auth.jwt.JwtProvider;
import com.samsamhajo.deepground.email.dto.EmailRequest;
import com.samsamhajo.deepground.email.repository.EmailVerificationRepository;
import com.samsamhajo.deepground.email.service.EmailService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public Long register(RegisterRequest request) {

        // 중복 검사
        checkEmailDuplicate(request.getEmail());
        checkNicknameDuplicate(request.getNickname());

        Member member = Member.createLocalMember(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname()
        );

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_EMAIL));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createRefreshToken(member.getId());

        return new LoginResponse(
                accessToken,
                refreshToken,
                member.getId(),
                member.getEmail(),
                member.getNickname()
        );
    }

    @Transactional
    public PasswordResetEmailResponse sendPasswordResetEmail(PasswordResetRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_EMAIL));

        EmailRequest emailRequest = new EmailRequest(request.getEmail());
        emailService.sendVerificationEmail(emailRequest);

        return PasswordResetEmailResponse.of(request.getEmail());
    }

    @Transactional
    public PasswordResetResponse resetPassword(PasswordResetVerifyRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_EMAIL));

        String storedCode = emailVerificationRepository.getCode(request.getEmail());
        if (storedCode == null) {
            throw new AuthException(AuthErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        if (!storedCode.equals(request.getCode())) {
            throw new AuthException(AuthErrorCode.INVALID_VERIFICATION_CODE);
        }

        member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);

        emailVerificationRepository.delete(request.getEmail());

        return PasswordResetResponse.of(member);
    }

    public void checkEmailDuplicate(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new AuthException(AuthErrorCode.DUPLICATE_EMAIL);
        }
    }

    public void checkNicknameDuplicate(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new AuthException(AuthErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
