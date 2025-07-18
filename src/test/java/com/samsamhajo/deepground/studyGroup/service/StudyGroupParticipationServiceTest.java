package com.samsamhajo.deepground.studyGroup.service;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupParticipationResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupMember;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupMemberRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import com.samsamhajo.deepground.chat.entity.ChatRoom;
import com.samsamhajo.deepground.chat.entity.ChatRoomType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Rollback
class StudyGroupParticipationServiceTest extends IntegrationTestSupport {

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private StudyGroupMemberRepository studyGroupMemberRepository;

  @Autowired
  private StudyGroupService studyGroupService;

  @Autowired
  private MemberRepository memberRepository;

  private Member member;
  private Member creator;

  @BeforeEach
  void setUp() {
    member = Member.createLocalMember("user@test.com", "pw", "사용자");
    creator = Member.createLocalMember("creator@test.com", "pw", "작성자");
    memberRepository.saveAll(List.of(member, creator));

    IntStream.rangeClosed(1, 5).forEach(i -> {
      StudyGroup group = StudyGroup.of(
          ChatRoom.of(ChatRoomType.STUDY_GROUP), "참가 스터디 " + i, "설명",
          LocalDate.now(), LocalDate.now().plusDays(30),
          LocalDate.now(), LocalDate.now().plusDays(10),
          5, creator, true, "서울"
      );
      studyGroupRepository.save(group);

      StudyGroupMember groupMember = StudyGroupMember.of(member, group, true);
      studyGroupMemberRepository.save(groupMember);
    });

    IntStream.rangeClosed(1, 2).forEach(i -> {
      StudyGroup group = StudyGroup.of(
          ChatRoom.of(ChatRoomType.STUDY_GROUP), "대기 스터디 " + i, "설명",
          LocalDate.now(), LocalDate.now().plusDays(30),
          LocalDate.now(), LocalDate.now().plusDays(10),
          5, creator, true, "서울"
      );
      studyGroupRepository.save(group);

      StudyGroupMember groupMember = StudyGroupMember.of(member, group, false); // 수락되지 않은 상태
      studyGroupMemberRepository.save(groupMember);
    });
  }

  @Test
  @DisplayName("사용자가 수락된(studyGroupMember.isAllowed = true) 스터디 목록만 조회할 수 있다")
  void findMyParticipatedGroups() {
    List<StudyGroupParticipationResponse> results =
        studyGroupService.getStudyGroupsByMember(member.getId());

    assertThat(results).hasSize(5);
    assertThat(results).allSatisfy(res -> {
      assertThat(res.getTitle()).contains("참가 스터디");
      assertThat(res.getCreatedBy()).isEqualTo("작성자");
    });
  }

  @Test
  @DisplayName("스터디에 참가하지 않은 사용자는 빈 리스트를 받는다")
  void emptyParticipationList() {
    Member outsider = Member.createLocalMember("empty@test.com", "pw", "외부인");
    memberRepository.save(outsider);

    List<StudyGroupParticipationResponse> results =
        studyGroupService.getStudyGroupsByMember(outsider.getId());

    assertThat(results).isEmpty();
  }
}
