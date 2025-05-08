package com.samsamhajo.deepground.calendar.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudySchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    @Column(name = "schedule_title", nullable = false)
    private String title;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "location")
    private String location;

    private StudySchedule(StudyGroup studyGroup, String title, LocalDateTime startTime, LocalDateTime endTime, String description, String location) {
        this.studyGroup = studyGroup;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.location = location;
    }

    // location이 있는 경우
    public static StudySchedule of(StudyGroup studyGroup, String title, LocalDateTime startTime, LocalDateTime endTime, String description, String location) {
        return new StudySchedule(studyGroup, title, startTime, endTime, description, location);
    }

    // location이 없는 경우
    public static StudySchedule of(StudyGroup studyGroup, String title, LocalDateTime startTime, LocalDateTime endTime, String description) {
        return new StudySchedule(studyGroup, title, startTime, endTime, description, null);
    }


}
