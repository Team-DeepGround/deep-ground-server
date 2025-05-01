package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
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

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "study_group_id", nullable = false)
    private Long studyGroupId;

    @Column(name = "is_allowed", nullable = false)
    private Boolean isAllowed = false;
}
