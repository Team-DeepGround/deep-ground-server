package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.chat.service.ChatRoomMemberService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.notification.entity.data.StudyGroupNotificationData;
import com.samsamhajo.deepground.notification.event.NotificationEvent;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupKickRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMemberSummary;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupInviteTokenRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.samsamhajo.deepground.member.utils.MemberUtils.extractProfileId;


@Service
@RequiredArgsConstructor
public class StudyGroupMemberService {
  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupMemberRepository studyGroupMemberRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public List<StudyGroupMemberSummary> getAcceptedMembers(Long studyGroupId) {
    StudyGroup group = studyGroupRepository.findById(studyGroupId)
        .orElseThrow(() -> new StudyGroupNotFoundException(studyGroupId));

    return studyGroupMemberRepository.findAcceptedMembersWithInfo(studyGroupId).stream()
        .map(member -> StudyGroupMemberSummary.builder()
                .memberId(member.getMember().getId())
                .profileId(extractProfileId(member.getMember()))
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
                .profileId(extractProfileId(m.getMember()))
                .nickname(m.getMember().getNickname())
                .build())
        .toList();
  }

  @Transactional
  public void requestToJoin(Member member, Long studyGroupId) {
    StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
        .orElseThrow(() -> new IllegalArgumentException("스터디 그룹이 존재하지 않습니다."));

    // 중복 요청 방지
    if (studyGroupMemberRepository.existsByMemberAndStudyGroup(member, studyGroup)) {
      throw new IllegalStateException("이미 참가 요청을 했거나 참여 중입니다.");
    }

    // 모집 기간 외 신청 방지
    LocalDate today = LocalDate.now();
    if (today.isBefore(studyGroup.getRecruitStartDate()) || today.isAfter(studyGroup.getRecruitEndDate())) {
      throw new IllegalStateException("스터디 그룹 모집 기간이 아닙니다.");
    }

    StudyGroupMember pendingRequest = StudyGroupMember.of(member, studyGroup, false);
    studyGroupMemberRepository.save(pendingRequest);

    // 스터디 그룹 가입 알림
    eventPublisher.publishEvent(NotificationEvent.of(
        studyGroup.getCreator().getId(),
        StudyGroupNotificationData.join(studyGroup)
    ));

  }

}
