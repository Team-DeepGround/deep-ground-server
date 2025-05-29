package com.samsamhajo.deepground.member.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.Dto.MemberProfileDto;
import com.samsamhajo.deepground.techStack.entity.MemberTechStack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member_profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_profile_id", nullable = false)
    private Long profileId;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "job" )
    private String job;

    @Column(name = "company")
    private String company;

    @Column(name ="live_in" , nullable = false)
    private String liveIn;

    @Column(name ="education")
    private String education;

    @OneToMany(mappedBy = "memberProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTechStack> memberTechStacks = new ArrayList<>();

    @Column(name ="github_url")
    private String githubUrl;

    @Column(name ="linkedIn_url")
    private String linkedInUrl;

    @Column(name ="website_url")
    private String websiteUrl;

    @Column(name = "twitter_url")
    private String twitterUrl;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    private MemberProfile(String profileImage, Member member, String introduction, String job, String company, String liveIn, String education, List<MemberTechStack> memberTechStacks,
                          String githubUrl, String linkedInUrl, String websiteUrl, String twitterUrl ){

        this.profileImage = profileImage;
        this.member = member;
        this.introduction = introduction;
        this.job = job;
        this.company= company;
        this.liveIn = liveIn;
        this.education = education;
        this.memberTechStacks = new ArrayList<>();
        for (MemberTechStack memberTechStack : memberTechStacks){
            memberTechStack.registerTo(this);
            this.memberTechStacks.add(memberTechStack);
        }
        this.githubUrl = githubUrl;
        this.linkedInUrl = linkedInUrl;
        this.websiteUrl = websiteUrl;
        this.twitterUrl = twitterUrl;
    }

    public static MemberProfile create(String profileImage, Member member, String introduction, String job, String company, String liveIn, String education,
                                       List<MemberTechStack> memberTechStacks, String githubUrl, String linkedInUrl, String websiteUrl, String twitterUrl) {

        return new MemberProfile( profileImage, member, introduction, job, company, liveIn, education, memberTechStacks,
                githubUrl, linkedInUrl, websiteUrl, twitterUrl);
    }

    public void update(MemberProfileDto dto) {
        this.profileImage = dto.getProfileImage();
        this.introduction = dto.getIntroduction();
        this.job = dto.getJob();
        this.company = dto.getCompany();
        this.liveIn = dto.getLiveIn();
        this.education = dto.getEducation();
        this.memberTechStacks.clear();
        for (MemberTechStack stack : memberTechStacks) {
            stack.registerTo(this);
            this.memberTechStacks.add(stack);
        }
        this.githubUrl = dto.getGithubUrl();
        this.linkedInUrl = dto.getLinkedInUrl();
        this.websiteUrl = dto.getWebsiteUrl();
        this.twitterUrl = dto.getTwitterUrl();
    }
}
