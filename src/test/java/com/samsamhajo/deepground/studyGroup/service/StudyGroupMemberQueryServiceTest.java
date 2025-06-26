package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMemberSummary;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StudyGroupMemberQueryServiceTest extends IntegrationTestSupport {

  @Autowired private StudyGroupMemberQueryService queryService;
  @Autowired private StudyGroupRepository studyGroupRepository;
  @Autowired private StudyGroupMemberRepository studyGroupMemberRepository;
  @Autowired private MemberRepository memberRepository;

  private Member owner;
  private Member memberA;
  private StudyGroup group;

  @BeforeEach
  void setUp() {
    owner = Member.createLocalMember("owner@test.com", "pw", "관리자");
    memberA = Member.createLocalMember("user@test.com", "pw", "사용자");
    memberRepository.save(owner);
    memberRepository.save(memberA);

    group = StudyGroup.of(
        null, "테스트 스터디", "설명",
        LocalDate.now(), LocalDate.now().plusDays(5),
        LocalDate.now(), LocalDate.now().plusDays(1),
        3, owner, true, "신촌",
        new HashSet<>()
    );
    studyGroupRepository.save(group);

    StudyGroupMember memberOwner = StudyGroupMember.of(owner, group, true);
    StudyGroupMember memberAJoined = StudyGroupMember.of(memberA, group, true);

    studyGroupMemberRepository.save(memberOwner);
    studyGroupMemberRepository.save(memberAJoined);
  }

  @Test
  @DisplayName("스터디 수락된 멤버 목록을 조회한다")
  void getAcceptedMembers() {
    List<StudyGroupMemberSummary> members = queryService.getAcceptedMembers(group.getId());

    assertThat(members).hasSize(2);
    assertThat(members.get(0).getNickname()).isEqualTo("관리자");
    assertThat(members.get(1).getNickname()).isEqualTo("사용자");
  }
}
