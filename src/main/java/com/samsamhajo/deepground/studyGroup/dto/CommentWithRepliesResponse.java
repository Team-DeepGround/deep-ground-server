package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupReply;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentWithRepliesResponse {
  private Long commentId;
  private String nickname;
  private String content;
  private LocalDateTime createdAt;
  private List<ReplyResponse> replies;

  public static CommentWithRepliesResponse from(StudyGroupComment comment, List<StudyGroupReply> replies) {
    return CommentWithRepliesResponse.builder()
        .commentId(comment.getId())
        .nickname(comment.getMember().getNickname())
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .replies(
            replies.stream()
                .map(ReplyResponse::from)
                .toList()
        )
        .build();
  }
}
