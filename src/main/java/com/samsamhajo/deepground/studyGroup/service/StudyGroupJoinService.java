package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import com.samsamhajo.deepground.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyGroupJoinService {

  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;

  @Transactional
  public void requestToJoin(Member member, Long studyGroupId) {
    StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
        .orElseThrow(() -> new IllegalArgumentException("스터디 그룹이 존재하지 않습니다."));

    // 중복 요청 방지
    if (studyGroupMemberRepository.existsByMemberAndStudyGroup(member, studyGroup)) {
      throw new IllegalStateException("이미 참가 요청을 했거나 참여 중입니다.");
    }

    StudyGroupMember pendingRequest = StudyGroupMember.of(member, studyGroup, false);
    studyGroupMemberRepository.save(pendingRequest);
  }
}
