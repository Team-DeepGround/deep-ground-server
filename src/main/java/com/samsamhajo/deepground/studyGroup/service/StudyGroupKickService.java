package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.chat.service.ChatRoomMemberService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupKickRequest;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyGroupKickService {

  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;
  private final ChatRoomMemberService chatRoomMemberService;

  @Transactional
  public void kickMember(StudyGroupKickRequest request, Member requester) {
    StudyGroup group = studyGroupRepository.findById(request.getStudyGroupId())
        .orElseThrow(() -> new StudyGroupNotFoundException(request.getStudyGroupId()));

    if (!group.getCreator().getId().equals(requester.getId())) {
      throw new IllegalArgumentException("스터디장만 강퇴할 수 있습니다.");
    }

    if (request.getTargetMemberId().equals(requester.getId())) {
      throw new IllegalArgumentException("자기 자신은 강퇴할 수 없습니다.");
    }

    StudyGroupMember target = studyGroupMemberRepository.findByStudyGroupIdAndMemberId(
            request.getStudyGroupId(), request.getTargetMemberId())
        .orElseThrow(() -> new IllegalArgumentException("대상 멤버가 스터디에 존재하지 않습니다."));

    // 채팅방 멤버 삭제
    chatRoomMemberService.leaveChatRoom(group.getChatRoom().getId(), target.getMember().getId());

    target.softDelete();
  }
}

