package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import static com.samsamhajo.deepground.studyGroup.entity.StudyGroupMemberStatus.APPROVED;
import static com.samsamhajo.deepground.studyGroup.entity.StudyGroupMemberStatus.NOT_APPLIED;

@Entity
@Getter
@Table(name = "study_group_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class StudyGroupMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @Enumerated
    private StudyGroupMemberStatus studyGroupMemberStatus;

    private StudyGroupMember(Member member, StudyGroup studyGroup,StudyGroupMemberStatus studyGroupMemberStatus) {
        this.member = member;
        this.studyGroup = studyGroup;
        this.studyGroupMemberStatus = studyGroupMemberStatus;
    }

    public static StudyGroupMember join(Member member, StudyGroup studyGroup) {
        return new StudyGroupMember(member, studyGroup ,StudyGroupMemberStatus.PENDING);
    }

    public static StudyGroupMember of(Member member, StudyGroup studyGroup) {
        return new StudyGroupMember(member, studyGroup , APPROVED);
    }

    public void allowMember() {
        this.studyGroupMemberStatus = APPROVED;
    }

    public void kickMember() {
        this.studyGroupMemberStatus = NOT_APPLIED;
    }
}
