package com.samsamhajo.deepground.auth.validator;

import com.samsamhajo.deepground.auth.exception.AuthErrorCode;
import com.samsamhajo.deepground.auth.exception.AuthException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterValidator {
    private final MemberRepository memberRepository;

    public void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new AuthException(AuthErrorCode.DUPLICATE_EMAIL);
        }
    }

    public void validateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new AuthException(AuthErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
