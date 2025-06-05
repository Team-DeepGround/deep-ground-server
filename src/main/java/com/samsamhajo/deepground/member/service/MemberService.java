package com.samsamhajo.deepground.member.service;

import com.samsamhajo.deepground.member.Dto.MemberProfileDto;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.entity.MemberProfile;
import com.samsamhajo.deepground.member.exception.MemberErrorCode;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.exception.ProfileErrorCode;
import com.samsamhajo.deepground.member.exception.ProfileException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.member.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public MemberProfileDto editMemberProfile(Long memberId, MemberProfileDto memberProfileDto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        member.updateNickname(memberProfileDto.getNickname());

        MemberProfile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.INVALID_PROFILE_ID));

        profile.update(memberProfileDto);

        MemberProfile saved = profileRepository.save(profile);

        return memberProfileDto.from(saved, member);
    }
}
