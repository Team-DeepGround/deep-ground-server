package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyGroupParticipationResponse {

  private Long studyGroupId;
  private String title;
  private String createdBy;
  private LocalDateTime participatedAt;

  public static StudyGroupParticipationResponse from(StudyGroup studyGroup, LocalDateTime participatedAt) {
    return StudyGroupParticipationResponse.builder()
        .studyGroupId(studyGroup.getId())
        .title(studyGroup.getTitle())
        .createdBy(studyGroup.getMember().getNickname())
        .participatedAt(participatedAt)
        .build();
  }
}
