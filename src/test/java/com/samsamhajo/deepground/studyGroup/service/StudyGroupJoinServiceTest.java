package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class StudyGroupJoinServiceTest extends IntegrationTestSupport {

  @Autowired
  private StudyGroupJoinService studyGroupJoinService;

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private StudyGroupMemberRepository studyGroupMemberRepository;

  @Autowired
  private MemberRepository memberRepository;

  @PersistenceContext
  private EntityManager em;

  private Member writer;
  private Member requester;
  private StudyGroup studyGroup;

  @BeforeEach
  void setUp() {
    writer = Member.createLocalMember("writer@test.com", "1234", "작성자");
    requester = Member.createLocalMember("requester@test.com", "1234", "요청자");

    memberRepository.save(writer);
    memberRepository.save(requester);

    studyGroup = StudyGroup.of(
        null, "스터디 제목", "스터디 설명",
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(10),
        LocalDate.now(),
        LocalDate.now().plusDays(5),
        5,
        writer,
        true,
        "강남",
        new HashSet<>()
    );
    studyGroupRepository.save(studyGroup);
    em.flush();
    em.clear();
  }

  @Test
  @DisplayName("스터디 그룹에 참가 요청을 성공적으로 보낼 수 있다")
  void requestJoin_success() {
    // when
    studyGroupJoinService.requestToJoin(requester, studyGroup.getId());

    // then
    boolean exists = studyGroupMemberRepository.existsByMemberAndStudyGroup(requester, studyGroup);
    assertThat(exists).isTrue();

    StudyGroupMember savedRequest = studyGroupMemberRepository.findByMemberAndStudyGroup(requester, studyGroup).orElseThrow();
    assertThat(savedRequest.getIsAllowed()).isFalse();
  }

  @Test
  @DisplayName("이미 요청을 보낸 경우 예외가 발생한다")
  void requestJoin_duplicateRequest_throws() {
    // given
    StudyGroupMember alreadyRequested = StudyGroupMember.of(requester, studyGroup, false);
    studyGroupMemberRepository.save(alreadyRequested);
    em.flush();
    em.clear();

    // when & then
    assertThatThrownBy(() -> studyGroupJoinService.requestToJoin(requester, studyGroup.getId()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("이미 참가 요청");
  }

  @Test
  @DisplayName("이미 참여 중인 경우 예외가 발생한다")
  void requestJoin_alreadyMember_throws() {
    // given
    StudyGroupMember joined = StudyGroupMember.of(requester, studyGroup, true);
    studyGroupMemberRepository.save(joined);
    em.flush();
    em.clear();

    // when & then
    assertThatThrownBy(() -> studyGroupJoinService.requestToJoin(requester, studyGroup.getId()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("참여 중");
  }
}
