package com.samsamhajo.deepground.studyGroup.repository;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.stream.IntStream;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@Rollback
class StudyGroupRepositoryTest extends IntegrationTestSupport {

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private MemberRepository memberRepository;

  private Member creator;

  @BeforeEach
  void setUp() {
    creator = Member.createLocalMember("repo@test.com", "1234", "저장자");
    memberRepository.save(creator);

    // 15개 모집중, 5개 모집종료, 다양한 키워드 포함
    IntStream.rangeClosed(1, 20).forEach(i -> {
      GroupStatus status = i <= 15 ? GroupStatus.RECRUITING : GroupStatus.COMPLETED;
      String title = i % 2 == 0 ? "자바 스터디" : "파이썬 스터디";
      String explanation = i % 3 == 0 ? "알고리즘 풀이" : "웹 개발";

      StudyGroup group = StudyGroup.of(
          null,
          title,
          explanation,
          LocalDate.now().plusDays(1),
          LocalDate.now().plusDays(30),
          LocalDate.now(),
          LocalDate.now().plusDays(5),
          5,
          creator,
          true,
          "강남"
      );
      group.changeGroupStatus(status);
      studyGroupRepository.save(group);
    });
  }

  @Test
  void findByGroupStatusAndTitleOrExplanation() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<StudyGroup> result = studyGroupRepository
        .findByGroupStatusAndTitleContainingIgnoreCaseOrGroupStatusAndExplanationContainingIgnoreCase(
            GroupStatus.RECRUITING, "자바",
            GroupStatus.RECRUITING, "자바",
            pageable
        );

    assertThat(result.getTotalElements()).isGreaterThan(0);
    assertThat(result.getContent()).allMatch(group ->
        group.getGroupStatus() == GroupStatus.RECRUITING &&
            (group.getTitle().contains("자바") || group.getExplanation().contains("자바"))
    );
  }

  @Test
  void findByTitleOrExplanation() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<StudyGroup> result = studyGroupRepository
        .findByTitleContainingIgnoreCaseOrExplanationContainingIgnoreCase("파이썬", "파이썬", pageable);

    assertThat(result.getTotalElements()).isGreaterThan(0);
    assertThat(result.getContent()).allMatch(group ->
        group.getTitle().contains("파이썬") || group.getExplanation().contains("파이썬")
    );
  }

  @Test
  void findByGroupStatus_onlyRecruiting() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<StudyGroup> result = studyGroupRepository.findByGroupStatus(GroupStatus.RECRUITING, pageable);

    assertThat(result.getTotalElements()).isEqualTo(15);
    assertThat(result.getContent()).allMatch(group -> group.getGroupStatus() == GroupStatus.RECRUITING);
  }

  @Test
  void findAll_pagingWorks() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<StudyGroup> page = studyGroupRepository.findAll(pageable);

    assertThat(page.getTotalElements()).isEqualTo(20);
    assertThat(page.getContent().size()).isEqualTo(10);
    assertThat(page.getTotalPages()).isEqualTo(2);
  }
}
