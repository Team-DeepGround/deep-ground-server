package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupMemberSummary {
  private Long memberId;
  private Long profileId;
  private String nickname;
  private boolean isOwner;
  private LocalDateTime joinedAt;
}
