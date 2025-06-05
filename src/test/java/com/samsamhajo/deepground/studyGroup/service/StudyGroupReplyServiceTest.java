package com.samsamhajo.deepground.studyGroup.service;

import static org.junit.jupiter.api.Assertions.*;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyRequest;
import com.samsamhajo.deepground.studyGroup.dto.StudyGroupReplyResponse;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroup;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupComment;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupCommentRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupReplyRepository;
import com.samsamhajo.deepground.studyGroup.repository.StudyGroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class StudyGroupReplyServiceTest extends IntegrationTestSupport {

  @Autowired private StudyGroupReplyService replyService;
  @Autowired private StudyGroupReplyRepository replyRepository;
  @Autowired private StudyGroupCommentRepository commentRepository;
  @Autowired private StudyGroupRepository groupRepository;
  @Autowired private MemberRepository memberRepository;

  @PersistenceContext private EntityManager em;

  private Long commentId;
  private Member writer;

  @BeforeEach
  void setUp() {
    writer = Member.createLocalMember("replyer@test.com", "pw", "답글작성자");
    Member commenter = Member.createLocalMember("commenter@test.com", "pw", "댓글작성자");
    memberRepository.save(writer);
    memberRepository.save(commenter);

    StudyGroup group = StudyGroup.of(
        null, "스터디 제목", "설명",
        LocalDate.now().plusDays(1), LocalDate.now().plusDays(3),
        LocalDate.now(), LocalDate.now().plusDays(2),
        5, commenter, true, "강남"
    );
    groupRepository.save(group);

    StudyGroupComment comment = StudyGroupComment.of(group, commenter, "원댓글입니다");
    commentRepository.save(comment);

    em.flush();
    em.clear();

    commentId = comment.getId();
  }

  @Test
  @DisplayName("부모 댓글 ID로 답글을 작성할 수 있다")
  void writeReply_success() {
    // given
    StudyGroupReplyRequest request = StudyGroupReplyRequest.builder()
        .commentId(commentId)
        .content("답글입니다")
        .build();

    // when
    StudyGroupReplyResponse response = replyService.writeReply(writer, request);

    // then
    assertThat(response.getParentCommentId()).isEqualTo(commentId);
    assertThat(response.getWriterNickname()).isEqualTo("답글작성자");
    assertThat(response.getContent()).isEqualTo("답글입니다");
    assertThat(response.getReplyId()).isNotNull();
    assertThat(response.getCreatedAt()).isNotNull();
  }

  @Test
  @DisplayName("존재하지 않는 댓글 ID로 답글을 작성하면 예외가 발생한다")
  void writeReply_commentNotFound() {
    // given
    StudyGroupReplyRequest request = StudyGroupReplyRequest.builder()
        .commentId(-1L)
        .content("답글")
        .build();

    // when & then
    assertThatThrownBy(() -> replyService.writeReply(writer, request))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
