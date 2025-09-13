package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.TechTag;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupCreateRequest {

  @NotBlank(message = "제목은 필수입니다.")
  @Size(min = 2, max = 30, message = "제목은 2자 이상 30자 이하로 입력해주세요.")
  private String title;

  @NotBlank(message = "설명은 필수입니다.")
  @Size(max = 500, message = "설명은 500자 이하로 입력해주세요.")
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
  @Min(value = 2, message = "정원은 최소 2명 이상이어야 합니다.")
  @Max(value = 10, message = "정원은 최대 10명까지 가능합니다.")
  private Integer maxMembers;

  @NotNull(message = "오프라인 여부는 필수입니다.")
  private Boolean isOffline;

  private String studyLocation; // isOffline이 true일 때만 필요

  @NotNull(message = "기술 스택은 필수입니다.")
  @Size(min = 1, message = "최소 1개 이상의 기술 스택을 선택해주세요.")
  private List<String> techStackNames;
}
