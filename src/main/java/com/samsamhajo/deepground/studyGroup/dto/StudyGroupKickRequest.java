package com.samsamhajo.deepground.studyGroup.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupKickRequest {
  @NotNull(message = "스터디 그룹 ID는 필수입니다.")
  private Long studyGroupId;

  @NotNull(message = "강퇴할 멤버 ID는 필수입니다.")
  private Long targetMemberId;
}
