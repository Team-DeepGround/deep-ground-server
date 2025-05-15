package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class StudyGroupResponse {
  private Long id;
  private String title;
  private String explanation;
  private GroupStatus groupStatus;
  private LocalDate recruitEndDate;
  private String studyLocation;

  public static StudyGroupResponse from(StudyGroup group) {
    return StudyGroupResponse.builder()
        .id(group.getId())
        .title(group.getTitle())
        .explanation(group.getExplanation())
        .groupStatus(group.getGroupStatus())
        .recruitEndDate(group.getRecruitEndDate())
        .studyLocation(group.getStudyLocation())
        .build();
  }
}
