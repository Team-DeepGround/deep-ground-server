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
  private Integer groupMemberCount;
  private Integer currentMemberCount;
  private LocalDate studyStartDate;
  private LocalDate studyEndDate;

  public static StudyGroupMyListResponse from(StudyGroup group) {
    return StudyGroupMyListResponse.builder()
            .id(group.getId())
            .title(group.getTitle())
            .createdAt(group.getCreatedAt().toLocalDate()) // BaseEntity 기반
            .groupStatus(group.getGroupStatus())
            .groupMemberCount(group.getGroupMemberCount())
            .currentMemberCount(group.getMembers().size())
            .studyStartDate(group.getStudyStartDate())
            .studyEndDate(group.getStudyEndDate())
            .build();
  }
}
