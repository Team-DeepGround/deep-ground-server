package com.samsamhajo.deepground.interest.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_interests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInterest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_interest_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id", nullable = false)
    private Interest interest;

    private MemberInterest(Member member, Interest interest) {
        this.member = member;
        this.interest = interest;
    }

    public static MemberInterest of(Member member, Interest interest) {
        return new MemberInterest(member,interest);
    }
}
