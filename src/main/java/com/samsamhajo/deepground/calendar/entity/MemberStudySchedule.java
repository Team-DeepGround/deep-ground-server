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

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "is_important", nullable = false)
    private Boolean isImportant = false;

    @Column(name = "memo")
    private String memo;

    private MemberStudySchedule(StudySchedule studySchedule, Boolean isAvailable, Boolean isImportant, String memo) {
        this.studySchedule = studySchedule;
        this.isAvailable = isAvailable;
        this.isImportant = isImportant;
        this.memo = memo;
    }

    public static MemberStudySchedule of(Member member, StudySchedule studySchedule, Boolean isAvailable, Boolean isImportant, String memo) {
        MemberStudySchedule memberStudySchedule =  new MemberStudySchedule(studySchedule, isAvailable, isImportant, memo);
        memberStudySchedule.member = member;
        return memberStudySchedule;
    }

    public void updateAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void updateImportant(Boolean isImportant) {
        this.isImportant = isImportant;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }
}
