package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupTechTag;
import com.samsamhajo.deepground.studyGroup.entity.TechTag;
import java.util.Set;
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
  private Set<StudyGroupTechTag> techTags;

  public static StudyGroupCreateResponse from(StudyGroup group) {
    return StudyGroupCreateResponse.builder()
        .id(group.getId())
        .title(group.getTitle())
        .explanation(group.getExplanation())
        .isOffline(group.getIsOffline())
        .studyLocation(group.getStudyLocation())
        .techTags(group.getStudyGroupTechTags())
        .build();
  }
}
