package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class StudyGroupParticipationResponse {

  private Long studyGroupId;
  private String title;
  private String createdBy;
  private LocalDateTime participatedAt;
  private GroupStatus groupStatus;
  private Integer groupMemberCount;
  private Integer currentMemberCount;
  private LocalDate studyStartDate;
  private LocalDate studyEndDate;
  private Boolean isOffline;

  public static StudyGroupParticipationResponse from(StudyGroup studyGroup, LocalDateTime participatedAt) {
    return StudyGroupParticipationResponse.builder()
        .studyGroupId(studyGroup.getId())
        .title(studyGroup.getTitle())
        .createdBy(studyGroup.getCreator().getNickname())
        .participatedAt(participatedAt)
            .groupStatus(studyGroup.getGroupStatus())
            .groupMemberCount(studyGroup.getGroupMemberCount())
            .currentMemberCount(studyGroup.getMembers().size())
            .studyStartDate(studyGroup.getStudyStartDate())
            .studyEndDate(studyGroup.getStudyEndDate())
            .isOffline(studyGroup.getIsOffline())
        .build();
  }
}
