package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupAcceptService {

  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;

  @Transactional
  public void acceptMember(Long studyGroupId, Long targetMemberId, Member requester) {
    // 스터디 존재 확인
    StudyGroup group = studyGroupRepository.findById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    // 요청자가 스터디장인지 확인
    if (!group.getCreator().getId().equals(requester.getId())) {
      throw new IllegalArgumentException("스터디장만 수락할 수 있습니다.");
    }

    // 신청자 존재 및 상태 확인
    StudyGroupMember member = studyGroupMemberRepository
        .findByStudyGroupIdAndMemberId(studyGroupId, targetMemberId)
        .orElseThrow(() -> new IllegalArgumentException("신청자가 존재하지 않습니다."));

    // 이미 수락된 경우 예외 처리
    if (Boolean.TRUE.equals(member.getIsAllowed())) {
      throw new IllegalStateException("이미 수락된 멤버입니다.");
    }

    // 상태 수락으로 변경
    member.allowMember();
  }
}