package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupDeleteService {

  private final StudyGroupRepository studyGroupRepository;

  @Transactional
  public void softDeleteStudyGroup(Long studyGroupId, Member requester) {
    var studyGroup = studyGroupRepository.findById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    if (!studyGroup.getMember().getId().equals(requester.getId())) {
      throw new IllegalArgumentException("스터디 생성자만 삭제할 수 있습니다.");
    }

    studyGroup.softDelete();
  }
}