package com.samsamhajo.deepground.question;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.comment.dto.CommentCreateResponse;
import com.samsamhajo.deepground.qna.comment.dto.CreateCommentRequest;
import com.samsamhajo.deepground.qna.comment.dto.UpdateCommentRequestDto;
import com.samsamhajo.deepground.qna.comment.dto.UpdateCommentResponseDto;
import com.samsamhajo.deepground.qna.comment.entity.Comment;
import com.samsamhajo.deepground.qna.comment.exception.CommentException;
import com.samsamhajo.deepground.qna.comment.repository.CommentRepository;
import com.samsamhajo.deepground.qna.comment.service.CommentService;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.validation.CommonValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommonValidation commonValidation;
    @Mock
    private MemberRepository memberRepository;

    private Question question;
    private Answer answer;
    private Comment comment;
    private Member member;
    private Member member1;

    @BeforeEach
    public void Mock회원() {
        member = Member.createLocalMember(
                "9636515@gmail.com",
                "test1234@",
                "Dotae"
        );

        member1 = Member.createLocalMember(
                "test@gmail.com",
                "test1224",
                "Guest"
        );
        question = Question.of(
                "테스트 제목",
                "테스트 내용",
                member
        );
        answer = Answer.of(
                "테스트 답변 내용",
                member,
                question
        );
        comment = Comment.of(
                "테스트 댓글 내용",
                member,
                answer
        );

        //Mock 객체를 위해 ID값 주입
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(member1, "id", 2L);
        ReflectionTestUtils.setField(question, "id", 1L);
        ReflectionTestUtils.setField(answer, "id", 1L);
        ReflectionTestUtils.setField(comment, "id", 1L);
    }

    @Test
    @DisplayName("댓글 작성 성공")
    public void commentTest() {

        String commentContent = "테스트 댓글 내용";
        CreateCommentRequest request = CreateCommentRequest.of(commentContent, answer.getId());

        //answerId가 호출되면, 우리가 만든 answer 객체 반환 -> 없으면 validation null point exception 발생
        when(commonValidation.AnswerValidation(answer.getId())).thenReturn(answer);

        //commentRepository에서 어떤 Comment객체라도 받게된다면, comment Return
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentCreateResponse response = commentService.createComment(request, member.getId());

        // comment, answer, memberId 일치 여부 + 우리가 작성한 commentContent내용 일치 여부 확인
        assertThat(response.getCommentId()).isEqualTo(comment.getId());
        assertThat(response.getCommentContent()).isEqualTo(commentContent);
        assertThat(response.getMemberId()).isEqualTo(member.getId());
        assertThat(response.getAnswerId()).isEqualTo(answer.getId());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    public void modifyCommentTest() {
        String commentContent = "테스트 댓글 내용";
        String modifyContent = "테스트 댓글 수정";

        UpdateCommentRequestDto request = UpdateCommentRequestDto.of(modifyContent, answer.getId(), comment.getId());

        when(commonValidation.CommentValidation(comment.getId())).thenReturn(comment);

        UpdateCommentResponseDto response = commentService.updateComment(request, member.getId());

        assertThat(response.getCommentContent()).isNotEqualTo(commentContent);
        assertThat(response.getMemberId()).isEqualTo(member.getId());

    }

    @Test
    @DisplayName("댓글 수정 실패 : 작성자가 아닌 경우")
    public void modifyCommentFailTest() {
        String modifyContent = "테스트 댓글 수정";
        UpdateCommentRequestDto request = UpdateCommentRequestDto.of(modifyContent, answer.getId(), comment.getId());

        when(commonValidation.CommentValidation(comment.getId())).thenReturn(comment);

        CommentException exception = assertThrows(CommentException.class, () -> {
            commentService.updateComment(request, member1.getId());
        });

        //예외 터지는 지 처리 여부
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("댓글을 작성한 멤버가 아닙니다.");
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    public void deleteCommentTest() {

        when(commonValidation.CommentValidation(comment.getId())).thenReturn(comment);

        commentService.deleteComment(comment.getId(), member.getId());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    @DisplayName("댓글 삭제 실패 : 작성자가 아닌 경우")
    public void deleteCommentFailTest() {

        when(commonValidation.CommentValidation(comment.getId())).thenReturn(comment);

        CommentException exception = assertThrows(CommentException.class, () -> {
            commentService.deleteComment(comment.getId(), member1.getId());
        });

        assertThat(exception.getErrorCode().getMessage()).isEqualTo("댓글을 작성한 멤버가 아닙니다.");
    }


}
