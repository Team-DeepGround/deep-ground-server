package com.samsamhajo.deepground.studyGroup.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StudyGroupCreateRequest {

  @NotBlank(message = "제목은 필수입니다.")
  private String title;

  @NotBlank(message = "설명은 필수입니다.")
  private String explanation;

  @NotNull(message = "스터디 시작일은 필수입니다.")
  private LocalDate studyStartDate;

  @NotNull(message = "스터디 종료일은 필수입니다.")
  private LocalDate studyEndDate;

  @NotNull(message = "모집 시작일은 필수입니다.")
  private LocalDate recruitStartDate;

  @NotNull(message = "모집 종료일은 필수입니다.")
  private LocalDate recruitEndDate;

  @NotNull(message = "정원은 필수입니다.")
  @Min(value = 1, message = "정원은 최소 1명 이상이어야 합니다.")
  private Integer groupMemberCount;

  @NotNull(message = "오프라인 여부는 필수입니다.")
  private Boolean isOffline;

  private String studyLocation; // isOffline이 true일 때만 필요
}
