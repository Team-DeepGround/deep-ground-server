package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupMyListResponse {

  private Long id;
  private String title;
  private LocalDate createdAt;
  private GroupStatus groupStatus;

  public static StudyGroupMyListResponse from(StudyGroup group) {
    return StudyGroupMyListResponse.builder()
        .id(group.getId())
        .title(group.getTitle())
        .createdAt(group.getCreatedAt().toLocalDate()) // BaseEntity 기반
        .groupStatus(group.getGroupStatus())
        .build();
  }
}
