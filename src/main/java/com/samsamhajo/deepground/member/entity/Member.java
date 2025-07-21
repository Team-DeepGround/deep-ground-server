package com.samsamhajo.deepground.member.entity;

import com.samsamhajo.deepground.communityPlace.entity.CommunityPlaceReview;
import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.interest.entity.MemberInterest;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.techStack.entity.MemberTechStack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password") // 소셜 로그인 시, 비밀번호는 필요가 없다.
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private Provider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.ROLE_USER;

    @OneToMany(mappedBy = "member")
    private List<MemberInterest> memberInterests = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<StudyGroupMember> studyGroupMembers = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "member_id")
    private List<CommunityPlaceReview> communityPlaceReviews = new ArrayList<>();

    private Member(String email, String password, String nickname, Provider provider, String providerId, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.isVerified = (provider != Provider.LOCAL);
        this.role = role;
    }

    //일반 회원가입 정적 메소드
    public static Member createLocalMember(String email, String password, String nickname) {
        return new Member(email, password, nickname, Provider.LOCAL, null, Role.ROLE_USER);
    }

    //소셜 로그인 용 정적 메소드
    public static Member createSocialMember(String email, String nickname, Provider provider, String providerId) {
        return new Member(email, null, nickname, provider, providerId, Role.ROLE_USER);
    }

    public void verify() {
        this.isVerified = true;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    // 소셜 로그인 시 닉네임 동기화
    public Member update(String nickname) {
        this.nickname = nickname;
        return this;
    }
}






