package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class StudyGroupDetailResponse {
  private Long id;
  private String title;
  private String explanation;
  private String writer;
  private int memberCount;
  private int groupLimit;
  private String location;
  private boolean isOffline;
  private String recruitEndDate;
  private int commentCount;
  private List<String> participants;

  public static StudyGroupDetailResponse from(StudyGroup group) {
    return StudyGroupDetailResponse.builder()
        .id(group.getId())
        .title(group.getTitle())
        .explanation(group.getExplanation())
        .writer(group.getCreator().getNickname())
        .memberCount(group.getMembers().size())
        .groupLimit(group.getGroupMemberCount())
        .location(group.getStudyLocation())
        .isOffline(group.getIsOffline())
        .recruitEndDate(group.getRecruitEndDate().toString())
        .commentCount(group.getComments().size())
        .participants(
            group.getMembers().stream()
                .map(m -> m.getMember().getNickname())
                .collect(Collectors.toList())
        )
        .build();
  }
}
