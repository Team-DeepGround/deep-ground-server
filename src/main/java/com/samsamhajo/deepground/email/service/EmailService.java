package com.samsamhajo.deepground.email.service;

import com.samsamhajo.deepground.email.dto.EmailRequest;
import com.samsamhajo.deepground.email.dto.EmailResponse;
import com.samsamhajo.deepground.email.dto.EmailVerifyRequest;
import com.samsamhajo.deepground.email.exception.EmailErrorCode;
import com.samsamhajo.deepground.email.exception.EmailException;
import com.samsamhajo.deepground.email.repository.EmailVerificationRepository;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final long VERIFICATION_CODE_TTL = 300L;
    private final MemberRepository memberRepository;

    public EmailResponse sendVerificationEmail(EmailRequest request) {
        String email = request.getEmail();
        String code = generateVerificationCode();

        // Redis에 인증 코드 저장
        emailVerificationRepository.save(email, code, VERIFICATION_CODE_TTL);
        // 이메일 발송
        sendEmail(email,
                "이메일 인증 코드",
                createVerificationEmailContent(code));

        return new EmailResponse(email, true);
    }

    @Transactional
    public boolean verifyEmail(EmailVerifyRequest request) {
        String email =request.getEmail();
        String storedCode = emailVerificationRepository.getCode(request.getEmail());

        if (storedCode == null) {
            throw new EmailException(EmailErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (!storedCode.equals(request.getCode())) {
            throw new EmailException(EmailErrorCode.INVALID_VERIFICATION_CODE);
        }

        // 인증 성공 시
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EmailException(EmailErrorCode.EMAIL_NOT_FOUND));

        member.verify();
        memberRepository.save(member);

        // 인증 성공 시 Redis에서 인증 코드 삭제
        emailVerificationRepository.delete(request.getEmail());

        return true;
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new EmailException(EmailErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    private String createVerificationEmailContent(String code) {
        return String.format("""
                인증 코드 : %s
                
                이 코드는 5분 동안 유효합니다.
                """, code);
    }

    public boolean isVerified(String email) {
        return !emailVerificationRepository.exists(email);
    }
}
