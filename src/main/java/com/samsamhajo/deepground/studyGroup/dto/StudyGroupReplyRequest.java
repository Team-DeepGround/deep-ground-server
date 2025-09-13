package com.samsamhajo.deepground.studyGroup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupReplyRequest {
  @NotNull(message = "부모 댓글 ID는 필수입니다.")
  private Long commentId;

  @NotBlank(message = "답글 내용은 비어 있을 수 없습니다.")
  @Size(max = 300, message = "답글은 300자 이하로 입력해주세요.")
  private String content;
}
