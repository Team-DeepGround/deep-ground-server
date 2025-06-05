package com.samsamhajo.deepground.studyGroup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupInviteRequest {
  @NotNull(message = "스터디 그룹 ID는 필수입니다.")
  private Long studyGroupId;

  @Email(message = "이메일 형식이 올바르지 않습니다.")
  @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
  private String inviteeEmail;
}
