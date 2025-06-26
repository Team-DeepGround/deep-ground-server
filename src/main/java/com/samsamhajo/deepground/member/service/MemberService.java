package com.samsamhajo.deepground.member.service;

import com.samsamhajo.deepground.global.upload.S3Uploader;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public MemberProfileDto editMemberProfile(Long memberId, MemberProfileDto memberProfileDto, MultipartFile profileImage) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        member.updateNickname(memberProfileDto.getNickname());

        MemberProfile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.INVALID_PROFILE_ID));

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = s3Uploader.upload(profileImage, "profile-images");
            memberProfileDto.setProfileImage(imageUrl);
        }

        profile.update(memberProfileDto);

        MemberProfile saved = profileRepository.save(profile);

        return memberProfileDto.from(saved, member);
    }
}
