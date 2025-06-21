package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupResponse;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupSearchRequest;
import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.stream.IntStream;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Rollback
class StudyGroupServiceSearchTest extends IntegrationTestSupport {

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StudyGroupService studyGroupService;

  private Member creator;

  @BeforeEach
  void setUp() {
    creator = Member.createLocalMember("test@test.com", "pw", "테스터");
    memberRepository.save(creator);

    IntStream.rangeClosed(1, 20).forEach(i -> {
      String title = (i % 2 == 0) ? "자바 스터디" : "파이썬 스터디";
      String explanation = (i % 3 == 0) ? "알고리즘 풀이" : "웹 개발";
      GroupStatus status = (i <= 15) ? GroupStatus.RECRUITING : GroupStatus.COMPLETED;

      StudyGroup group = StudyGroup.of(
          null, title, explanation,
          LocalDate.now().plusDays(1),
          LocalDate.now().plusDays(30),
          LocalDate.now(),
          LocalDate.now().plusDays(5),
          5, creator, true, "강남",
          new HashSet<>()
      );
      group.changeGroupStatus(status);
      studyGroupRepository.save(group);
    });
  }

  @Test
  @DisplayName("키워드와 모집 상태를 기반으로 스터디 목록을 검색할 수 있다")
  void searchByKeywordAndStatus() {
    StudyGroupSearchRequest request = StudyGroupSearchRequest.builder()
        .keyword("자바")
        .groupStatus(GroupStatus.RECRUITING)
        .page(0)
        .size(10)
        .build();

    Page<StudyGroupResponse> result = studyGroupService.searchStudyGroups(request);

    assertThat(result.getTotalElements()).isGreaterThan(0);
    assertThat(result.getContent())
        .allSatisfy(res -> {
          assertThat(res.getGroupStatus()).isEqualTo(GroupStatus.RECRUITING);
          assertThat(res.getTitle()).contains("자바");
        });
  }

  @Test
  @DisplayName("키워드만으로 스터디 목록을 검색할 수 있다")
  void searchByKeywordOnly() {
    StudyGroupSearchRequest request = StudyGroupSearchRequest.builder()
        .keyword("파이썬")
        .page(0)
        .size(10)
        .build();

    Page<StudyGroupResponse> result = studyGroupService.searchStudyGroups(request);

    assertThat(result.getTotalElements()).isGreaterThan(0);
    assertThat(result.getContent())
        .allSatisfy(res -> assertThat(res.getTitle()).contains("파이썬"));
  }

  @Test
  @DisplayName("모집 상태만으로 스터디 목록을 검색할 수 있다")
  void searchByStatusOnly() {
    StudyGroupSearchRequest request = StudyGroupSearchRequest.builder()
        .groupStatus(GroupStatus.RECRUITING)
        .page(0)
        .size(10)
        .build();

    Page<StudyGroupResponse> result = studyGroupService.searchStudyGroups(request);

    assertThat(result.getTotalElements()).isEqualTo(15);
    assertThat(result.getContent())
        .allSatisfy(res -> assertThat(res.getGroupStatus()).isEqualTo(GroupStatus.RECRUITING));
  }

  @Test
  @DisplayName("필터 없이 전체 목록을 페이징 처리하여 가져올 수 있다")
  void searchWithoutFilter() {
    StudyGroupSearchRequest request = StudyGroupSearchRequest.builder()
        .page(0)
        .size(10)
        .build();

    Page<StudyGroupResponse> result = studyGroupService.searchStudyGroups(request);

    assertThat(result.getTotalElements()).isEqualTo(20);
    assertThat(result.getContent()).hasSize(10);
  }
}
