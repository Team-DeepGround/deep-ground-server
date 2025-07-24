package com.samsamhajo.deepground.studyGroup.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupKickRequest;
import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomType;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import java.time.LocalDate;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class StudyGroupKickServiceTest extends IntegrationTestSupport {

  @Autowired
  private StudyGroupMemberService studyGroupMemberService;
  @Autowired private MemberRepository memberRepository;
  @Autowired private StudyGroupRepository studyGroupRepository;
  @Autowired private StudyGroupMemberRepository studyGroupMemberRepository;

  private Member owner;
  private Member target;
  private Member outsider;
  private StudyGroup group;

  @BeforeEach
  void setUp() {
    owner = Member.createLocalMember("owner@test.com", "pw", "운영자");
    target = Member.createLocalMember("target@test.com", "pw", "강퇴대상");
    outsider = Member.createLocalMember("outsider@test.com", "pw", "외부인");
    memberRepository.save(owner);
    memberRepository.save(target);
    memberRepository.save(outsider);

    group = StudyGroup.of(
        ChatRoom.of(ChatRoomType.STUDY_GROUP), "스터디", "소개",
        LocalDate.now(), LocalDate.now().plusDays(10),
        LocalDate.now(), LocalDate.now().plusDays(3),
        5, owner, true, "강남"
    );
    studyGroupRepository.save(group);

    StudyGroupMember targetMember = StudyGroupMember.of(target, group, true);
    studyGroupMemberRepository.save(targetMember);
  }

  @Test
  @DisplayName("스터디장이 멤버를 강퇴하면 성공한다")
  void kickSuccess() {
    // given
    StudyGroupKickRequest request = StudyGroupKickRequest.builder()
        .studyGroupId(group.getId())
        .targetMemberId(target.getId())
        .build();

    // when
    studyGroupMemberService.kickMember(request, owner);

    // then
    assertThat(studyGroupMemberRepository.findByStudyGroupIdAndMemberId(group.getId(), target.getId())).isEmpty();
  }

  @Test
  @DisplayName("스터디장이 아닌 사용자가 강퇴 시도 시 예외 발생")
  void kickByUnauthorized() {
    StudyGroupKickRequest request = StudyGroupKickRequest.builder()
        .studyGroupId(group.getId())
        .targetMemberId(target.getId())
        .build();

    assertThatThrownBy(() -> studyGroupMemberService.kickMember(request, outsider))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("스터디장이 본인을 강퇴하려고 하면 예외 발생")
  void kickSelf() {
    StudyGroupKickRequest request = StudyGroupKickRequest.builder()
        .studyGroupId(group.getId())
        .targetMemberId(owner.getId())
        .build();

    assertThatThrownBy(() -> studyGroupMemberService.kickMember(request, owner))
        .isInstanceOf(IllegalArgumentException.class);
  }
}