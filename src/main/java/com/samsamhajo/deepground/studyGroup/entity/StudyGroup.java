package com.samsamhajo.deepground.studyGroup.entity;

import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "study_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_group_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
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

    @Column(name = "is_offline", nullable = false)
    private Boolean isOffline;

    @Column(name = "study_location")
    private String studyLocation;

    @OneToMany(mappedBy = "studyGroup", orphanRemoval = true)
    private Set<StudyGroupTechTag> studyGroupTechTags = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member creator;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "studyGroup", fetch = FetchType.EAGER)
    private final Set<StudyGroupMember> members = new HashSet<>();

    @OneToMany(mappedBy = "studyGroup")
    private final List<StudyGroupComment> comments = new ArrayList<>();

    private StudyGroup(
        ChatRoom chatRoom, String title, String explanation,
        LocalDate studyStartDate, LocalDate studyEndDate,
        LocalDate recruitStartDate, LocalDate recruitEndDate,
        Integer groupMemberCount, Member member, Boolean isOffline, String studyLocation
    ) {
        this.chatRoom = chatRoom;
        this.title = title;
        this.explanation = explanation;
        this.studyStartDate = studyStartDate;
        this.studyEndDate = studyEndDate;
        this.recruitStartDate = recruitStartDate;
        this.recruitEndDate = recruitEndDate;
        this.groupMemberCount = groupMemberCount;
        this.creator = member;
        this.isOffline = isOffline;
        this.studyLocation = studyLocation;
    }

    public static StudyGroup of(
        ChatRoom chatRoom, String title, String explanation,
        LocalDate studyStartDate, LocalDate studyEndDate,
        LocalDate recruitStartDate, LocalDate recruitEndDate,
        Integer groupMemberCount, Member member, Boolean isOffline, String studyLocation
    ) {
        return new StudyGroup(
            chatRoom, title, explanation,
            studyStartDate, studyEndDate,
            recruitStartDate, recruitEndDate,
            groupMemberCount, member, isOffline, studyLocation
        );
    }

    public void changeGroupStatus(GroupStatus newStatus) {
        this.groupStatus = newStatus;
    }

    public void addTechTag(StudyGroupTechTag techTag) {
        this.studyGroupTechTags.add(techTag);
    }
}