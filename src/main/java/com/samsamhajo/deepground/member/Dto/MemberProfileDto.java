package com.samsamhajo.deepground.member.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.entity.MemberProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberProfileDto {
    private String email;

    private String profileImage;

    @NotBlank (message = "닉네임을 입력해주세요")
    private String nickname;

    @NotBlank(message = "자기소개를 입력해주세요.")
    private String introduction;

    @NotBlank(message = "직업을 입력해주세요.")
    private String job;

    @NotBlank(message = "회사를 입력해주세요.")
    private String company;

    @NotBlank (message = "사는 지역을 입력해주세요")
    private String liveIn;

    @NotBlank (message = "학력을 입력해주세요.")
    private String education;

    @NotNull (message = "한가지 이상의 기술 스택을 입력해주세요")
    @Builder.Default
    private List<String> techStack = new ArrayList<>();

    @URL(message = "올바른 URL 형식이 아닙니다.")
    private String githubUrl;

    @URL(message = "올바른 URL 형식이 아닙니다.")
    private String linkedInUrl;

    @URL(message = "올바른 URL 형식이 아닙니다.")
    private String websiteUrl;

    @URL(message = "올바른 URL 형식이 아닙니다.")
    private String twitterUrl;

    public static MemberProfileDto from(MemberProfile profile, Member member) {

        return MemberProfileDto.builder()
                .email(member.getEmail())
                .profileImage(profile.getProfileImage())
                .nickname(member.getNickname())
                .introduction(profile.getIntroduction())
                .job(profile.getJob())
                .company(profile.getCompany())
                .liveIn(profile.getLiveIn())
                .education(profile.getEducation())
                .techStack(profile.getMemberTechStacks().stream()
                        .map(stack -> stack.getTechStack().getName())
                        .collect(Collectors.toList()))
                .githubUrl(profile.getGithubUrl())
                .linkedInUrl(profile.getLinkedInUrl())
                .websiteUrl(profile.getWebsiteUrl())
                .twitterUrl(profile.getTwitterUrl())
                .build();
    }

    public static MemberProfileDto of(MemberProfile profile) {

        return MemberProfileDto.builder()
                .email(profile.getMember().getEmail())
                .profileImage(profile.getProfileImage())
                .nickname(profile.getMember().getNickname())
                .introduction(profile.getIntroduction())
                .job(profile.getJob())
                .company(profile.getCompany())
                .liveIn(profile.getLiveIn())
                .education(profile.getEducation())
                .techStack(profile.getMemberTechStacks().stream()
                        .map(stack -> stack.getTechStack().getName())
                        .collect(Collectors.toList()))
                .githubUrl(profile.getGithubUrl())
                .linkedInUrl(profile.getLinkedInUrl())
                .websiteUrl(profile.getWebsiteUrl())
                .twitterUrl(profile.getTwitterUrl())
                .build();
    }
}
