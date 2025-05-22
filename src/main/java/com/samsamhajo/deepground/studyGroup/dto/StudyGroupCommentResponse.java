package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyGroupCommentResponse {
  private Long commentId;
  private String writerNickname;
  private String content;
  private LocalDateTime createdAt;

  public static StudyGroupCommentResponse from(Long commentId, String writerNickname,
      String content, LocalDateTime createdAt) {
    return StudyGroupCommentResponse.builder()
        .commentId(commentId)
        .writerNickname(writerNickname)
        .content(content)
        .createdAt(createdAt).build();
  }
}
