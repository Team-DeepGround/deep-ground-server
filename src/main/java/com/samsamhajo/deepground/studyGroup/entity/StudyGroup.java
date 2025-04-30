package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_groups")
public class StudyGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_group_id")
    private Long id;

    @Column(name = "chat_room_id", nullable = false)
    private Long chatRoomId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "explanation", nullable = false, columnDefinition = "TEXT")
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_status", nullable = false)
    private GroupStatus groupStatus = GroupStatus.RECRUITING;

    @Column(name = "study_start_date", nullable = false)
    private LocalDate studyStartDate;

    @Column(name = "study_end_date", nullable = false)
    private LocalDate studyEndDate;

    @Column(name = "recruit_start_date", nullable = false)
    private LocalDate recruitStartDate;

    @Column(name = "recruit_end_date", nullable = false)
    private LocalDate recruitEndDate;

    @Column(name = "group_member_count", nullable = false)
    private Integer groupMemberCount;

    @Column(name = "founder_id", nullable = false)
    private Long founderId;

    @Column(name = "is_offline", nullable = false)
    private Boolean isOffline;

    @Column(name = "study_location")
    private String studyLocation;
}
