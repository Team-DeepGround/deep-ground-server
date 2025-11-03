package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupMemberSummary {
  private UUID memberPublicId;
  private UUID profilePublicId;
  private String nickname;
  private boolean isOwner;
  private LocalDateTime joinedAt;
}
