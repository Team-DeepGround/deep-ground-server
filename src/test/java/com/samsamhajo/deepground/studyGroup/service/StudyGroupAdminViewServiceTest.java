// StudyGroupAdminViewServiceTest
package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupAdminViewResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
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
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class StudyGroupAdminViewServiceTest extends IntegrationTestSupport {

  @Autowired
  private StudyGroupAdminViewService adminViewService;

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private StudyGroupMemberRepository studyGroupMemberRepository;

  @Autowired
  private MemberRepository memberRepository;

  @PersistenceContext
  private EntityManager em;

  private Member writer;
  private Member approvedUser;
  private Member waitingUser;
  private Long studyGroupId;

  @BeforeEach
  void setUp() {
    writer = Member.createLocalMember("host@test.com", "1234", "운영자");
    approvedUser = Member.createLocalMember("user1@test.com", "1234", "참여자1");
    waitingUser = Member.createLocalMember("user2@test.com", "1234", "대기자");

    memberRepository.save(writer);
    memberRepository.save(approvedUser);
    memberRepository.save(waitingUser);

    StudyGroup group = StudyGroup.of(
        null, "운영 테스트 스터디", "운영자 확인용",
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(10),
        LocalDate.now(),
        LocalDate.now().plusDays(5),
        10,
        writer,
        true,
        "역삼"
    );
    studyGroupRepository.save(group);

    studyGroupMemberRepository.save(StudyGroupMember.of(approvedUser, group, true));
    studyGroupMemberRepository.save(StudyGroupMember.of(waitingUser, group, false));

    em.flush();
    em.clear();

    studyGroupId = group.getId();
  }

  @Test
  @DisplayName("스터디 운영자가 관리 정보 요청 시 참가자 상태 및 통계를 조회할 수 있다")
  void getAdminView_success() {
    // when
    StudyGroupAdminViewResponse response = adminViewService.getAdminView(writer, studyGroupId);

    // then
    assertThat(response.getStudyTitle()).isEqualTo("운영 테스트 스터디");
    assertThat(response.getTotalMembers()).isEqualTo(2);
    assertThat(response.getApprovedCount()).isEqualTo(1);
    assertThat(response.getWaitingCount()).isEqualTo(1);

    assertThat(response.getMembers())
        .extracting(StudyGroupAdminViewResponse.MemberStatusDto::getNickname)
        .containsExactlyInAnyOrder("참여자1", "대기자");

    assertThat(response.getMembers())
        .anyMatch(m -> m.getNickname().equals("참여자1") && Boolean.TRUE.equals(m.getIsAllowed()));

    assertThat(response.getMembers())
        .anyMatch(m -> m.getNickname().equals("대기자") && Boolean.FALSE.equals(m.getIsAllowed()));
  }

  @Test
  @DisplayName("스터디 운영자가 아닌 사용자가 요청 시 예외가 발생한다")
  void getAdminView_accessDenied() {
    // when & then
    assertThatThrownBy(() -> adminViewService.getAdminView(waitingUser, studyGroupId))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  @DisplayName("존재하지 않는 스터디 ID로 요청 시 예외 발생")
  void getAdminView_notFound() {
    // when & then
    assertThatThrownBy(() -> adminViewService.getAdminView(writer, -999L))
        .hasMessageContaining("스터디 그룹을 찾을 수 없습니다");
  }
}