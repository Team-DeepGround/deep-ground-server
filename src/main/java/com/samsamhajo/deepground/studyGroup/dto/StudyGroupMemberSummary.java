package com.samsamhajo.deepground.studyGroup.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupMemberSummary {
  private Long memberId;
  private String nickname;
  private boolean isOwner;
  private LocalDateTime joinedAt;
}