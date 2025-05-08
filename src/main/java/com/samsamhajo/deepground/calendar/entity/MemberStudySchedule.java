package com.samsamhajo.deepground.calendar.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_study_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberStudySchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_study_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_schedule_id")
    private StudySchedule studySchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "memo")
    private String memo;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "is_important", nullable = false)
    private Boolean isImportant = false;

    private MemberStudySchedule(StudySchedule studySchedule, Member member, String memo, Boolean isAvailable, Boolean isImportant) {
        this.studySchedule = studySchedule;
        this.member = member;
        this.memo = memo;
        this.isAvailable = isAvailable;
        this.isImportant = isImportant;
    }

    public static MemberStudySchedule of(StudySchedule studySchedule, Member member, String memo, Boolean isAvailable, Boolean isImportant) {
        return new MemberStudySchedule(studySchedule, member, memo, isAvailable, isImportant);
    }


}
