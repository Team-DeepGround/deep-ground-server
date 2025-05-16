//package com.samsamhajo.deepground.studyGroup.repository;
//
//import com.samsamhajo.deepground.IntegrationTestSupport;
//import com.samsamhajo.deepground.member.entity.Member;
//import com.samsamhajo.deepground.member.repository.MemberRepository;
//import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
//import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
//import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.samsamhajo.deepground.studyGroup.entity.GroupStatus;
//import org.springframework.data.domain.*;
//import java.util.stream.IntStream;
//import org.springframework.test.annotation.Rollback;
//
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//
//
//@Transactional
//@Rollback
//class StudyGroupRepositoryTest extends IntegrationTestSupport {
//
//  @Autowired
//  private StudyGroupRepository studyGroupRepository;
//
//  @Autowired
//  private MemberRepository memberRepository;
//
//  @PersistenceContext
//  private EntityManager em;
//
//  private StudyGroup savedGroup;
//
//  @BeforeEach
//  void setUp() {
//    Member writer = Member.createLocalMember("writer@test.com", "1234", "작성자");
//    Member participant = Member.createLocalMember("user@test.com", "1234", "참여자");
//    memberRepository.save(writer);
//    memberRepository.save(participant);
//
//    StudyGroup group = StudyGroup.of(
//        null, "테스트 스터디", "설명입니다",
//        LocalDate.now().plusDays(1),
//        LocalDate.now().plusDays(10),
//        LocalDate.now(),
//        LocalDate.now().plusDays(5),
//        10,
//        writer,
//        true,
//        "강남"
//    );
//    savedGroup = studyGroupRepository.save(group);
//    em.flush();
//    em.clear();
//
//    StudyGroupMember groupMember = StudyGroupMember.of(participant, savedGroup, true);
//    StudyGroupComment comment = StudyGroupComment.of(savedGroup, participant, "댓글입니다");
//
//    em.persist(groupMember);
//    em.persist(comment);
//
//    em.flush();
//    em.clear();
//  }
//
//  @Test
//  @DisplayName("스터디 그룹 ID로 상세 조회 시 작성자, 멤버 목록, 댓글 목록이 함께 조회된다")
//  void findWithMemberAndCommentsById() {
//    Optional<StudyGroup> result = studyGroupRepository.findWithMemberAndCommentsById(savedGroup.getId());
//
//    assertThat(result).isPresent();
//    StudyGroup group = result.get();
//
//    assertThat(group.getTitle()).isEqualTo("테스트 스터디");
//    assertThat(group.getMember()).isNotNull(); // 작성자
//    assertThat(group.getMembers()).hasSize(1); // 그룹 멤버 1명
//    assertThat(group.getComments()).hasSize(1); // 댓글 1개
//
//  private Member creator;
//
//  @BeforeEach
//  void setUp() {
//    creator = Member.createLocalMember("repo@test.com", "1234", "저장자");
//    memberRepository.save(creator);
//
//    // 15개 모집중, 5개 모집종료, 다양한 키워드 포함
//    IntStream.rangeClosed(1, 20).forEach(i -> {
//      GroupStatus status = i <= 15 ? GroupStatus.RECRUITING : GroupStatus.COMPLETED;
//      String title = i % 2 == 0 ? "자바 스터디" : "파이썬 스터디";
//      String explanation = i % 3 == 0 ? "알고리즘 풀이" : "웹 개발";
//
//      StudyGroup group = StudyGroup.of(
//          null,
//          title,
//          explanation,
//          LocalDate.now().plusDays(1),
//          LocalDate.now().plusDays(30),
//          LocalDate.now(),
//          LocalDate.now().plusDays(5),
//          5,
//          creator,
//          true,
//          "강남"
//      );
//      group.changeGroupStatus(status);
//      studyGroupRepository.save(group);
//    });
//  }
//
//  @Test
//  void findByGroupStatusAndTitleOrExplanation() {
//    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
//    Page<StudyGroup> result = studyGroupRepository
//        .findByGroupStatusAndTitleContainingIgnoreCaseOrGroupStatusAndExplanationContainingIgnoreCase(
//            GroupStatus.RECRUITING, "자바",
//            GroupStatus.RECRUITING, "자바",
//            pageable
//        );
//
//    assertThat(result.getTotalElements()).isGreaterThan(0);
//    assertThat(result.getContent()).allMatch(group ->
//        group.getGroupStatus() == GroupStatus.RECRUITING &&
//            (group.getTitle().contains("자바") || group.getExplanation().contains("자바"))
//    );
//  }
//
//  @Test
//  void findByTitleOrExplanation() {
//    Pageable pageable = PageRequest.of(0, 10);
//    Page<StudyGroup> result = studyGroupRepository
//        .findByTitleContainingIgnoreCaseOrExplanationContainingIgnoreCase("파이썬", "파이썬", pageable);
//
//    assertThat(result.getTotalElements()).isGreaterThan(0);
//    assertThat(result.getContent()).allMatch(group ->
//        group.getTitle().contains("파이썬") || group.getExplanation().contains("파이썬")
//    );
//  }
//
//  @Test
//  void findByGroupStatus_onlyRecruiting() {
//    Pageable pageable = PageRequest.of(0, 10);
//    Page<StudyGroup> result = studyGroupRepository.findByGroupStatus(GroupStatus.RECRUITING, pageable);
//
//    assertThat(result.getTotalElements()).isEqualTo(15);
//    assertThat(result.getContent()).allMatch(group -> group.getGroupStatus() == GroupStatus.RECRUITING);
//  }
//
//  @Test
//  void findAll_pagingWorks() {
//    Pageable pageable = PageRequest.of(0, 10);
//    Page<StudyGroup> page = studyGroupRepository.findAll(pageable);
//
//    assertThat(page.getTotalElements()).isEqualTo(20);
//    assertThat(page.getContent().size()).isEqualTo(10);
//    assertThat(page.getTotalPages()).isEqualTo(2);
//  }
//}
