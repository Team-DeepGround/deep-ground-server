package com.samsamhajo.deepground.question;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import com.samsamhajo.deepground.qna.answer.service.AnswerService;
import com.samsamhajo.deepground.qna.comment.dto.CommentCreateRequestDto;
import com.samsamhajo.deepground.qna.comment.dto.CommentCreateResponseDto;
import com.samsamhajo.deepground.qna.comment.exception.CommentErrorCode;
import com.samsamhajo.deepground.qna.comment.exception.CommentException;
import com.samsamhajo.deepground.qna.comment.repository.CommentRepository;
import com.samsamhajo.deepground.qna.comment.service.CommentService;
import com.samsamhajo.deepground.qna.question.Dto.QuestionCreateRequestDto;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class CommentDeleteTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionService questionService;

    private Long memberId;
    private Long memberId2;

    @BeforeEach
    public void 회원_저장() {
        Member member = Member.createLocalMember("2@gmail.com", "asd", "dotae");
        memberRepository.save(member);
        memberId = member.getId();

        Member member2 = Member.createLocalMember("2@gmail.com", "asd", "dotae");
        memberRepository.save(member2);
        memberId2 = member2.getId();
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void testDeleteComment() {

            String title = "테스트";
            String content = "테스트1";

            String answerContent = "test answercontent";

            List<Long> techStack = List.of(1L, 2L);

            List<MultipartFile> mediaFiles = List.of(
                    new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
            );

            //질문 생성
            QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStack, mediaFiles);
            Question question = questionService.createQuestion(questionCreateRequestDto, memberId);
            Long questionId = question.getId();

            //답변 생성
            AnswerCreateRequestDto answerCreateRequestDto = new AnswerCreateRequestDto(answerContent, mediaFiles, questionId);
            AnswerCreateResponseDto answerCreateResponseDto = answerService.createAnswer(answerCreateRequestDto, memberId);
            Long answerId = answerCreateResponseDto.getAnswerId();

            //댓글 생성
            String commentContent = "테스트 댓글 내용";
            CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto(commentContent, answerId);
            CommentCreateResponseDto commentCreateResponseDto = commentService.createComment(commentCreateRequestDto, memberId);
            Long commentId = commentCreateResponseDto.getCommentId();
            System.out.println(commentId);

            //댓글 삭제
            Long deleteId = commentService.deleteComment(commentId, memberId, answerId);

            //삭제한 id가 commentid와 동일한지
            assertThat(deleteId.equals(commentId)).isTrue();
            assertThat(commentRepository.findById(commentId).isPresent()).isFalse();
        }
        @Test
    @DisplayName("삭제 예외 처리")
    public void testDeleteComment2() {

            String title = "테스트";
            String content = "테스트1";

            String answerContent = "test answercontent";

            List<Long> techStack = List.of(1L, 2L);

            List<MultipartFile> mediaFiles = List.of(
                    new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
            );

            //질문 생성
            QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStack, mediaFiles);
            Question question = questionService.createQuestion(questionCreateRequestDto, memberId);
            Long questionId = question.getId();

            //답변 생성
            AnswerCreateRequestDto answerCreateRequestDto = new AnswerCreateRequestDto(answerContent, mediaFiles, questionId);
            AnswerCreateResponseDto answerCreateResponseDto = answerService.createAnswer(answerCreateRequestDto, memberId);
            AnswerCreateResponseDto answerCreateResponseDto2 = answerService.createAnswer(answerCreateRequestDto, memberId);
            Long answerId = answerCreateResponseDto.getAnswerId();
            Long answerId2 = answerCreateResponseDto2.getAnswerId();


            //댓글 생성
            String commentContent = "테스트 댓글 내용";
            CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto(commentContent, answerId);
            CommentCreateResponseDto commentCreateResponseDto = commentService.createComment(commentCreateRequestDto, memberId);
            Long commentId = commentCreateResponseDto.getCommentId();
            System.out.println(commentId);

            CommentException commentException = assertThrows(CommentException.class, () -> commentService.deleteComment(commentId, memberId2, answerId));
            CommentException commentException2 = assertThrows(CommentException.class, () -> commentService.deleteComment(commentId, memberId, answerId2));

            assertThat(commentException.getMessage()).isEqualTo(CommentErrorCode.COMMENT_MEMBER_MISMATCH.getMessage());
            assertThat(commentException2.getMessage()).isEqualTo(CommentErrorCode.COMMENT_ANSWER_MISMATCH.getMessage());



        }
}
