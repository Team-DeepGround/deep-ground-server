package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "study_group_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id")
    private Long id;

    @OneToMany(mappedBy = "studyGroupMember")
    @JoinColumn(name = "member_id", nullable = false)
    private final List<Member> studyMember = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @Column(name = "is_allowed", nullable = false)
    private Boolean isAllowed = false;

    private StudyGroupMember(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public static StudyGroupMember of(StudyGroup studyGroup) {
        return new StudyGroupMember(studyGroup);
    }

    public void allowMember() {
        this.isAllowed = true;
    }

    public void banMember() {
        this.isAllowed = false;
    }
}
