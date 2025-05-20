package com.samsamhajo.deepground.studyGroup.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupCommentResponse {
  private Long commentId;
  private String writerNickname;
  private String content;
  private LocalDateTime createdAt;
}
