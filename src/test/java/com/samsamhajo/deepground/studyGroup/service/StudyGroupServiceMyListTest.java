package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupMyListResponse;
import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Rollback
@TestPropertySource(properties = {
    "JWT_SECRET=dGhpcy1pcy1hLXNlY3VyZS10ZXN0LXNlY3JldC1rZXktZm9yLWp3dA=="
})
class StudyGroupServiceMyListTest extends IntegrationTestSupport {

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StudyGroupService studyGroupService;

  private Member creator;
  private Member otherUser;

  @BeforeEach
  void setUp() {
    creator = Member.createLocalMember("creator@test.com", "pw", "생성자");
    otherUser = Member.createLocalMember("other@test.com", "pw", "다른 사용자");
    memberRepository.save(creator);
    memberRepository.save(otherUser);

    IntStream.rangeClosed(1, 10).forEach(i -> {
      StudyGroup group = StudyGroup.of(
          null, "스터디 " + i, "설명",
          LocalDate.now().plusDays(1),
          LocalDate.now().plusDays(30),
          LocalDate.now(),
          LocalDate.now().plusDays(5),
          5, creator, true, "강남"
      );
      if (i > 5) {
        group.changeGroupStatus(GroupStatus.COMPLETED);
      }
      studyGroupRepository.save(group);
    });

    // 다른 사용자가 만든 스터디도 3개 생성
    IntStream.rangeClosed(1, 3).forEach(i -> {
      StudyGroup group = StudyGroup.of(
          null, "타인 스터디 " + i, "설명",
          LocalDate.now().plusDays(1),
          LocalDate.now().plusDays(30),
          LocalDate.now(),
          LocalDate.now().plusDays(5),
          5, otherUser, true, "신촌"
      );
      studyGroupRepository.save(group);
    });
  }

  @Test
  @DisplayName("특정 사용자가 생성한 스터디 목록을 최신순으로 조회할 수 있다")
  void findStudyGroupsByCreator() {
    List<StudyGroupMyListResponse> results = studyGroupService.findMyStudyGroups(creator.getId());

    assertThat(results).hasSize(10);
    assertThat(results)
        .isSortedAccordingTo((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
    assertThat(results)
        .allSatisfy(res -> assertThat(res.getTitle()).contains("스터디"));
  }

  @Test
  @DisplayName("스터디를 생성하지 않은 사용자는 빈 리스트를 받는다")
  void returnEmptyListWhenNoStudyGroups() {
    Member emptyMember = Member.createLocalMember("empty@test.com", "pw", "빈사용자");
    memberRepository.save(emptyMember);

    List<StudyGroupMyListResponse> results = studyGroupService.findMyStudyGroups(emptyMember.getId());

    assertThat(results).isEmpty();
  }
}
