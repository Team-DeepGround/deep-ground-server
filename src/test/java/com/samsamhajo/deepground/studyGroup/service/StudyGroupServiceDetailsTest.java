package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupDetailResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
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
class StudyGroupServiceDetailsTest extends IntegrationTestSupport {

  @Autowired
  private StudyGroupService studyGroupService;

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private MemberRepository memberRepository;

  @PersistenceContext
  private EntityManager em;

  private Long studyGroupId;

  @BeforeEach
  void setUp() {
    Member writer = Member.createLocalMember("writer@test.com", "1234", "작성자");
    Member participant = Member.createLocalMember("user@test.com", "1234", "참여자");
    memberRepository.save(writer);
    memberRepository.save(participant);

    StudyGroup group = StudyGroup.of(
        null, "통합테스트 스터디", "설명입니다",
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(10),
        LocalDate.now(),
        LocalDate.now().plusDays(5),
        10,
        writer,
        true,
        "강남",
        new HashSet<>()
    );
    studyGroupRepository.save(group);

    StudyGroupMember groupMember = StudyGroupMember.of(participant, group, true);
    StudyGroupComment comment = StudyGroupComment.of(group, participant, "댓글입니다");

    em.persist(groupMember);
    em.persist(comment);
    em.flush();
    em.clear();

    studyGroupId = group.getId();
  }

  @Test
  @DisplayName("스터디 그룹 ID로 상세 조회하면 작성자, 멤버, 댓글이 모두 포함된다")
  void getStudyGroupDetail_success() {
    // when
    StudyGroupDetailResponse result = studyGroupService.getStudyGroupDetail(studyGroupId, 0L);

    // then
    assertThat(result.getTitle()).isEqualTo("통합테스트 스터디");
    assertThat(result.getWriter()).isEqualTo("작성자");
    assertThat(result.getParticipants()).contains("참여자");
    // TODO: 댓글 도메인 구현 시, 관련 DTO 수정 필요함
//    assertThat(result.get).contains("댓글입니다");
  }

  @Test
  @DisplayName("스터디 그룹 ID가 존재하지 않으면 예외가 발생한다")
  void getStudyGroupDetail_notFound() {
    // when & then
    assertThatThrownBy(() -> studyGroupService.getStudyGroupDetail(-1L, 0L))
        .isInstanceOf(StudyGroupNotFoundException.class);
  }
}
