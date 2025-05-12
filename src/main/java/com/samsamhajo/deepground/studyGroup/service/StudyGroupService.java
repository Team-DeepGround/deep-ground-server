package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.studyGroup.dto.StudyGroupDetailResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

  private final StudyGroupRepository studyGroupRepository;

  public StudyGroupDetailResponse getStudyGroupDetail(Long studyGroupId) {
    StudyGroup group = studyGroupRepository.findWithMemberAndCommentsById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    return StudyGroupDetailResponse.from(group);
  }
}
