package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupAdminViewResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyGroupAdminViewService {
  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;

  public StudyGroupAdminViewResponse getAdminView(Member requester, Long studyGroupId) {
    StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    if (!studyGroup.getCreator().getId().equals(requester.getId())) {
      throw new AccessDeniedException("해당 스터디 그룹의 생성자가 아닙니다.");
    }

    List<StudyGroupMember> memberList =
        studyGroupMemberRepository.findAllWithMemberByStudyGroupId(studyGroupId);

    int approved = studyGroupMemberRepository.countByStudyGroup_IdAndIsAllowedTrue(studyGroupId);
    int waiting = studyGroupMemberRepository.countByStudyGroup_IdAndIsAllowedFalse(studyGroupId);

    List<StudyGroupAdminViewResponse.MemberStatusDto> members = memberList.stream()
        .map(m -> StudyGroupAdminViewResponse.MemberStatusDto.builder()
            .memberId(m.getMember().getId())
            .nickname(m.getMember().getNickname())
            .isAllowed(m.getIsAllowed())
            .build())
        .collect(Collectors.toList());

    return StudyGroupAdminViewResponse.builder()
        .studyGroupId(studyGroup.getId())
        .studyTitle(studyGroup.getTitle())
        .totalMembers(members.size())
        .approvedCount(approved)
        .waitingCount(waiting)
        .members(members)
        .build();
  }
}
