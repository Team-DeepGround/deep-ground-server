package com.samsamhajo.deepground.techStack.entity;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.entity.MemberProfile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_tech_stacks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_tech_stack_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id", nullable = false)
    private MemberProfile memberProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_stack_id", nullable = false)
    private TechStack techStack;

    private MemberTechStack(Member member, MemberProfile memberProfile, TechStack techStack) {
        this.member = member;
        this.memberProfile = memberProfile;
        this.techStack = techStack;
    }

    public static MemberTechStack of(Member member,MemberProfile memberProfile,TechStack techStack){
        return new MemberTechStack(member,memberProfile, techStack);
    }

    public void registerTo(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
    }
}