package com.samsamhajo.deepground.studyGroup.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyGroupAdminViewResponse {
  private Long studyGroupId;
  private String studyTitle;
  private int totalMembers;
  private int approvedCount;
  private int waitingCount;
  private List<MemberStatusDto> members;

  @Getter
  @Builder
  public static class MemberStatusDto {
    private Long memberId;
    private String nickname;
    private Boolean isAllowed;
  }
}
