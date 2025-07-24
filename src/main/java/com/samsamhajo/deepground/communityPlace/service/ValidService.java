package com.samsamhajo.deepground.communityPlace.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidService {

    private final MemberRepository memberRepository;

    /**
     * 추후에 기능 개발이 더 들어가면 공통적으로 Validation관련 중복 코드가 증가할 것 같아 미리 만들어 두었습니다.
     */
    public Long findMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));
        return member.getId();
    }


}
