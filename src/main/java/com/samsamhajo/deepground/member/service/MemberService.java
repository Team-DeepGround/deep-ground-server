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
import com.samsamhajo.deepground.techStack.entity.MemberTechStack;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.exception.TechStackErrorCode;
import com.samsamhajo.deepground.techStack.exception.TechStackException;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;
    private final TechStackRepository techStackRepository;

    @Transactional
    public MemberProfileDto editMemberProfile(Long memberId, MemberProfileDto memberProfileDto, MultipartFile profileImage) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        member.updateNickname(memberProfileDto.getNickname());

        MemberProfile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.INVALID_PROFILE_ID));

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = s3Uploader.upload(profileImage, "profile-images");
            memberProfileDto.setProfileImage(imageUrl);
        }

        profile.update(memberProfileDto);

        profile.getMemberTechStacks().clear();
        List<MemberTechStack> updatedStacks = memberProfileDto.getTechStack().stream()
                .map(name -> {
                    TechStack techStack = techStackRepository.findByName(name)
                            .orElseThrow(() -> new TechStackException(TechStackErrorCode.TECH_STACK_NOT_FOUND));
                    return MemberTechStack.of(member, profile, techStack);
                })
                .toList();
        updatedStacks.forEach(profile.getMemberTechStacks()::add);

        MemberProfile saved = profileRepository.save(profile);

        return memberProfileDto.from(saved, member);
    }

    @Transactional
    public MemberProfileDto createProfile(Long memberId, MemberProfileDto dto, MultipartFile profileImage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.INVALID_MEMBER_ID));

        if (profileRepository.findByMemberId(memberId).isPresent()) {
            throw new ProfileException(ProfileErrorCode.ALREADY_EXISTS_PROFILE);
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = s3Uploader.upload(profileImage, "profile-images");
            dto.setProfileImage(imageUrl);
        }

        MemberProfile profile = MemberProfile.create(
                dto.getProfileImage(),
                member,
                dto.getIntroduction(),
                dto.getJob(),
                dto.getCompany(),
                dto.getLiveIn(),
                dto.getEducation(),
                new ArrayList<>(),
                dto.getGithubUrl(),
                dto.getLinkedInUrl(),
                dto.getWebsiteUrl(),
                dto.getTwitterUrl()
        );

        List<MemberTechStack> techStacks = dto.getTechStack().stream()
                .map(name -> {
                    TechStack techStack = techStackRepository.findByName(name)
                            .orElseThrow(() -> new TechStackException(TechStackErrorCode.TECH_STACK_NOT_FOUND));
                    return MemberTechStack.of(member, profile, techStack);
                })
                .toList();
        techStacks.forEach(profile.getMemberTechStacks()::add);

        profileRepository.save(profile);
        return MemberProfileDto.of(profile);
    }

    @Transactional(readOnly = true)
    public MemberProfileDto getMyProfile(Long memberId) {
        MemberProfile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.INVALID_PROFILE_ID));
        return MemberProfileDto.of(profile);
    }

    // TODO: 비공개 프로필 설정 도입 시, memberId와 profileId를 비교하여 접근 제한 예외 처리 추가 예정
    @Transactional(readOnly = true)
    public MemberProfileDto getUserProfile(Long memberId, Long profileId) {

        MemberProfile profile = profileRepository.findById(profileId)
                .orElseThrow(()-> new ProfileException(ProfileErrorCode.INVALID_PROFILE_ID));


        return MemberProfileDto.of(profile);
    }
}
