package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.chat.service.ChatRoomMemberService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.notification.entity.data.StudyGroupNotificationData;
import com.samsamhajo.deepground.notification.event.NotificationEvent;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupAdminViewResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupKickRequest;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyGroupAdminService {
  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;
  private final ChatRoomMemberService chatRoomMemberService;
  private final ApplicationEventPublisher eventPublisher;

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

  @jakarta.transaction.Transactional
  public void acceptMember(Long studyGroupId, Long targetMemberId, Member requester) {
    // 스터디 존재 확인
    StudyGroup group = studyGroupRepository.findByIdForUpdate(studyGroupId)
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

    // 채팅방 참가
    chatRoomMemberService.joinChatRoom(member.getMember(), group.getChatRoom());

    // 스터디 그룹 가입 알림
    eventPublisher.publishEvent(NotificationEvent.of(
        targetMemberId,
        StudyGroupNotificationData.accept(group)
    ));

  }

  @Transactional
  public void kickMember(StudyGroupKickRequest request, Member requester) {
    StudyGroup group = studyGroupRepository.findByIdForUpdate(request.getStudyGroupId())
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
    if (target.getIsAllowed()) {
      chatRoomMemberService.leaveChatRoom(group.getChatRoom().getId(), target.getMember().getId());

      // 스터디 그룹 강퇴 알림
      eventPublisher.publishEvent(NotificationEvent.of(
          request.getTargetMemberId(),
          StudyGroupNotificationData.kick(group)
      ));
    }

    target.softDelete();
  }
}
