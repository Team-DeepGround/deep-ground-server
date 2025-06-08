package com.samsamhajo.deepground.studyGroup.service;


import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMemberSummary;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
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
            .isOwner(group.getMember().getId().equals(member.getMember().getId()))
            .joinedAt(member.getCreatedAt())
            .build())
        .collect(Collectors.toList());
  }
}