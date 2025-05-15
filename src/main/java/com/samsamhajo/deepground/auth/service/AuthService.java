package com.samsamhajo.deepground.auth.service;

import com.samsamhajo.deepground.auth.dto.RegisterRequest;
import com.samsamhajo.deepground.auth.validator.RegisterValidator;
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
    private final RegisterValidator registerValidator;

    @Transactional
    public Long register(RegisterRequest request) {

        // 중복 검사
        registerValidator.validateEmail(request.getEmail());
        registerValidator.validateNickname(request.getNickname());

        Member member = Member.createLocalMember(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname()
        );

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}
