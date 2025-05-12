package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.studyGroup.dto.StudyGroupResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupSearchRequest;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

  private final StudyGroupRepository studyGroupRepository;

  public Page<StudyGroupResponse> searchStudyGroups(StudyGroupSearchRequest request) {
    String keyword = request.getKeyword();
    var status = request.getGroupStatus();
    var pageable = request.toPageable();

    Page<StudyGroup> pageResult;

    if (keyword != null && !keyword.isBlank()) {
      if (status != null) {
        pageResult = studyGroupRepository
            .findByGroupStatusAndTitleContainingIgnoreCaseOrGroupStatusAndExplanationContainingIgnoreCase(
                status, keyword, status, keyword, pageable
            );
      } else {
        pageResult = studyGroupRepository
            .findByTitleContainingIgnoreCaseOrExplanationContainingIgnoreCase(keyword, keyword, pageable);
      }
    } else {
      if (status != null) {
        pageResult = studyGroupRepository.findByGroupStatus(status, pageable);
      } else {
        pageResult = studyGroupRepository.findAll(pageable);
      }
    }

    return pageResult.map(StudyGroupResponse::from);
  }
}
