package com.samsamhajo.deepground.studyGroup.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyGroupReplyResponse {
  private Long replyId;
  private Long parentCommentId;
  private String writerNickname;
  private String content;
  private LocalDateTime createdAt;
}

