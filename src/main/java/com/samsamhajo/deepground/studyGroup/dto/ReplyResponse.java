package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroupReply;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyResponse {
  private Long replyId;
  private String nickname;
  private String content;
  private LocalDateTime createdAt;

  public static ReplyResponse from(StudyGroupReply reply) {
    return ReplyResponse.builder()
        .replyId(reply.getId())
        .nickname(reply.getMember().getNickname())
        .content(reply.getContent())
        .createdAt(reply.getCreatedAt())
        .build();
  }
}
