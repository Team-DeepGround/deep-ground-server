package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
public class StudyGroupSearchRequest {

  private final String keyword;
  private final GroupStatus groupStatus;
  private final int page;
  private final int size;
  private final List<String> techStackNames;

  @Builder
  public StudyGroupSearchRequest(
      String keyword,
      GroupStatus groupStatus,
      int page,
      int size,
      List<String> techStackNames
  ) {
    this.keyword = keyword;
    this.groupStatus = groupStatus;
    this.page = page;
    this.size = size;
    this.techStackNames = techStackNames;
  }

  public Pageable toPageable() {
    return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
  }
}