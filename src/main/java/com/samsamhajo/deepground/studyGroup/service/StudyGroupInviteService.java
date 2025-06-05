package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupInviteRequest;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupInviteToken;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupInviteTokenRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudyGroupInviteService {

  private final StudyGroupRepository studyGroupRepository;
  private final StudyGroupInviteTokenRepository inviteTokenRepository;

  @Transactional
  public void inviteByEmail(Member inviter, StudyGroupInviteRequest request) {
    StudyGroup group = studyGroupRepository.findById(request.getStudyGroupId())
        .orElseThrow(() -> new IllegalArgumentException("스터디 그룹이 존재하지 않습니다."));

    if (!group.getMember().getId().equals(inviter.getId())) {
      throw new SecurityException("스터디 생성자만 초대할 수 있습니다.");
    }

    if (inviteTokenRepository.existsByStudyGroup_IdAndEmail(group.getId(), request.getInviteeEmail())) {
      throw new IllegalStateException("이미 초대된 사용자입니다.");
    }

    String token = UUID.randomUUID().toString();
    LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

    StudyGroupInviteToken inviteToken = StudyGroupInviteToken.of(
        group, request.getInviteeEmail(), token, expiresAt
    );

    inviteTokenRepository.save(inviteToken);
    // TODO: 이메일 발송 로직 연동
  }
}

