package com.samsamhajo.deepground.auth.service;

import com.samsamhajo.deepground.auth.dto.RegisterRequest;
import com.samsamhajo.deepground.auth.exception.AuthErrorCode;
import com.samsamhajo.deepground.auth.exception.AuthException;
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
