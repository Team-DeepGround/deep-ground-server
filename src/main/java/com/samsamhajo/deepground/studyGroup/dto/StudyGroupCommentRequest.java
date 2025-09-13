package com.samsamhajo.deepground.studyGroup.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupCommentRequest {
  @NotNull(message = "스터디 그룹 ID는 필수입니다.")
  private Long studyGroupId;

  @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
  @Size(max = 300, message = "댓글은 300자 이하로 입력해주세요.")
  private String content;
}
