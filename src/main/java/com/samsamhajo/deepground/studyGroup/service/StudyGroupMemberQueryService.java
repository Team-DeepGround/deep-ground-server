package com.samsamhajo.deepground.studyGroup.service;


import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMemberSummary;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyGroupMemberQueryService {

  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;

  @Transactional
  public List<StudyGroupMemberSummary> getAcceptedMembers(Long studyGroupId) {
    StudyGroup group = studyGroupRepository.findById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    return studyGroupMemberRepository.findAcceptedMembersWithInfo(studyGroupId).stream()
        .map(member -> StudyGroupMemberSummary.builder()
            .memberId(member.getMember().getId())
            .nickname(member.getMember().getNickname())
            .isOwner(group.getCreator().getId().equals(member.getMember().getId()))
            .joinedAt(member.getCreatedAt())
            .build())
        .toList();
  }

  public List<StudyGroupMemberSummary> getPendingApplicantsAsCreator(Long studyGroupId, Member requester) {
    StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    if (!studyGroup.getCreator().getId().equals(requester.getId())) {
      throw new IllegalArgumentException("스터디장만 신청자 목록을 조회할 수 있습니다.");
    }

    List<StudyGroupMember> pending = studyGroupMemberRepository.findAllByStudyGroupIdAndIsAllowedFalse(studyGroupId);

    return pending.stream()
        .map(m -> StudyGroupMemberSummary.builder()
            .memberId(m.getMember().getId())
            .nickname(m.getMember().getNickname())
            .build())
        .toList();
  }
}