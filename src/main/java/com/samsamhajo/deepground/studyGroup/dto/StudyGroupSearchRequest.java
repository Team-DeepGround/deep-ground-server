package com.samsamhajo.deepground.studyGroup.dto;

import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.TechTag;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public class StudyGroupSearchRequest {

  private final String keyword;
  private final GroupStatus groupStatus;
  private final int page;
  private final int size;
  private final Set<TechTag> techTags; // 추가됨

  @Builder
  public StudyGroupSearchRequest(String keyword, GroupStatus groupStatus, int page, int size, Set<TechTag> techTags) {
    this.keyword = keyword;
    this.groupStatus = groupStatus;
    this.page = page;
    this.size = size;
    this.techTags = techTags;
  }

  public Pageable toPageable() {
    return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
  }
}
