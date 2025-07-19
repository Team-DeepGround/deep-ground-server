package com.samsamhajo.deepground.member.entity;

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

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "naver_id")
    private String naverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.ROLE_USER;

    @OneToMany(mappedBy = "member")
    private List<MemberInterest> memberInterests = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<StudyGroupMember> studyGroupMembers = new ArrayList<>();

    private Member(String email, String password, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.isVerified = (password == null);
        this.role = role;
    }

    //일반 회원가입 정적 메소드
    public static Member createLocalMember(String email, String password, String nickname) {
        return new Member(email, password, nickname, Role.ROLE_USER);
    }

    //소셜 로그인 용 정적 메소드
    public static Member createSocialMember(String email, String nickname) {
        return new Member(email, null, nickname, Role.ROLE_USER);
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

    public void linkSocialAccount(Provider provider, String socialId) {
        switch (provider) {
            case GOOGLE -> this.googleId = socialId;
            case NAVER -> this.naverId = socialId;
        }
        this.isVerified = true;
    }
}






