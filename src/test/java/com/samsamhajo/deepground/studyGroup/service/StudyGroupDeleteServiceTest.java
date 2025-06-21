package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.exception.StudyGroupNotFoundException;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
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
class StudyGroupDeleteServiceTest extends IntegrationTestSupport {

  @Autowired private StudyGroupDeleteService deleteService;
  @Autowired private StudyGroupRepository studyGroupRepository;
  @Autowired private MemberRepository memberRepository;

  private Member owner;
  private Member other;
  private StudyGroup group;

  @BeforeEach
  void setUp() {
    owner = Member.createLocalMember("owner@test.com", "pw", "운영자");
    other = Member.createLocalMember("other@test.com", "pw", "다른 사용자");
    memberRepository.save(owner);
    memberRepository.save(other);

    group = StudyGroup.of(
        null, "삭제 테스트 스터디", "설명",
        LocalDate.now(), LocalDate.now().plusDays(10),
        LocalDate.now(), LocalDate.now().plusDays(3),
        5, owner, true, "강남",
        new HashSet<>()
    );
    studyGroupRepository.save(group);
  }

  @Test
  @DisplayName("스터디 생성자가 스터디를 소프트 삭제할 수 있다")
  void deleteByOwner() {
    // when
    deleteService.softDeleteStudyGroup(group.getId(), owner);

    // then
    StudyGroup deleted = studyGroupRepository.findById(group.getId())
        .orElseThrow(() -> new AssertionError("스터디가 존재해야 합니다"));

    assertThat(deleted.isDeleted()).isTrue();
  }

  @Test
  @DisplayName("스터디 생성자가 아닌 사용자가 삭제 요청 시 예외가 발생한다")
  void deleteByNonOwner() {
    // expect
    assertThatThrownBy(() -> deleteService.softDeleteStudyGroup(group.getId(), other))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("존재하지 않는 스터디 삭제 시 예외가 발생한다")
  void deleteNonExistentGroup() {
    // expect
    assertThatThrownBy(() -> deleteService.softDeleteStudyGroup(-1L, owner))
        .isInstanceOf(StudyGroupNotFoundException.class);
  }
}