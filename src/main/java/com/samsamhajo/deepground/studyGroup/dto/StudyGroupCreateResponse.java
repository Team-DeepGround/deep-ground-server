package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyGroupCreateResponse {

  private Long id;
  private String title;
  private String explanation;
  private Boolean isOffline;
  private String studyLocation;

  public static StudyGroupCreateResponse from(StudyGroup group) {
    return StudyGroupCreateResponse.builder()
        .id(group.getId())
        .title(group.getTitle())
        .explanation(group.getExplanation())
        .isOffline(group.getIsOffline())
        .studyLocation(group.getStudyLocation())
        .build();
  }
}
