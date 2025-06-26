package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupReply;
import java.time.LocalDate;
import java.util.Map;
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
  private LocalDate recruitStartDate;
  private LocalDate recruitEndDate;
  private LocalDate studyStartDate;
  private LocalDate studyEndDate;
  private int commentCount;
  private List<String> participants;
  private List<CommentWithRepliesResponse> comments;

  public static StudyGroupDetailResponse from(StudyGroup group, Map<Long, List<StudyGroupReply>> replyMap) {
    return StudyGroupDetailResponse.builder()
        .id(group.getId())
        .title(group.getTitle())
        .explanation(group.getExplanation())
        .writer(group.getCreator().getNickname())
        .memberCount(group.getMembers().size())
        .groupLimit(group.getGroupMemberCount())
        .location(group.getStudyLocation())
        .isOffline(group.getIsOffline())
        .recruitStartDate(group.getRecruitStartDate())
        .recruitEndDate(group.getRecruitEndDate())
        .studyStartDate(group.getStudyStartDate())
        .studyEndDate(group.getStudyEndDate())
        .commentCount(group.getComments().size())
        .participants(
            group.getMembers().stream()
                .map(m -> m.getMember().getNickname())
                .collect(Collectors.toList())
        )
        .comments(
            group.getComments().stream()
                .distinct()
                .map(comment -> CommentWithRepliesResponse.from(comment, replyMap.getOrDefault(comment.getId(), List.of())))
                .toList()
        )
        .build();
  }
}