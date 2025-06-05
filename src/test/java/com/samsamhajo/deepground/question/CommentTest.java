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
import org.junit.jupiter.api.Assertions;
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

@SpringBootTest
@Transactional
public class CommentTest {

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
    private Long memberId;
    private Long questionId;
    private Long answerId;
    private Long commentId;
    @Autowired
    private QuestionService questionService;

    @BeforeEach
    public void 회원_저장() {
        Member member = Member.createLocalMember("2@gmail.com", "asd", "dotae");
        memberRepository.save(member);
        memberId = member.getId();
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    public void createCommentTest() {

        String title = "테스트";
        String content = "테스트1";

        String answerContent = "test answercontent";

        List<Long> techStack = List.of(1L, 2L);

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        //질문 생성
        QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStack, mediaFiles);
        Question question = questionService.createQuestion(questionCreateRequestDto,memberId);
        questionId = question.getId();

        //답변 생성
        AnswerCreateRequestDto answerCreateRequestDto = new AnswerCreateRequestDto(answerContent, mediaFiles, questionId);
        AnswerCreateResponseDto answerCreateResponseDto = answerService.createAnswer(answerCreateRequestDto, memberId);
        answerId = answerCreateResponseDto.getAnswerId();

        //댓글 생성
        String commentContent = "테스트 댓글 내용";
        CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto(commentContent, answerId);
        CommentCreateResponseDto commentCreateResponseDto = commentService.createComment(commentCreateRequestDto, memberId);
        commentId = commentCreateResponseDto.getCommentId();
        System.out.println(commentId);

        //작성한 댓글이 맞게 들어갔는지
        assertThat(commentCreateResponseDto.getCommentContent()).isEqualTo(commentContent);
        //맞는 답변에 댓글이 달렸는지
        assertThat(commentCreateResponseDto.getAnswerId()).isEqualTo(answerId);
        //생성된 commentID가 repository에 저장된 id와 동일한지
        assertThat(commentRepository.findById(commentId)).isNotNull();
    }

    @Test
    @DisplayName("댓글_길이제한_예외처리테스트")
    public void commentExceptionTest() {

        String title = "테스트";
        String content = "테스트1";

        String answerContent = "test answercontent";

        List<Long> techStack = List.of(1L, 2L);

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        //질문 생성
        QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStack, mediaFiles);
        Question question = questionService.createQuestion(questionCreateRequestDto,memberId);
        questionId = question.getId();

        //답변 생성
        AnswerCreateRequestDto answerCreateRequestDto = new AnswerCreateRequestDto(answerContent, mediaFiles, questionId);
        AnswerCreateResponseDto answerCreateResponseDto = answerService.createAnswer(answerCreateRequestDto, memberId);
        answerId = answerCreateResponseDto.getAnswerId();

        String commentContent = "";
        try {
            CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto(commentContent, answerId);
            CommentCreateResponseDto commentCreateResponseDto = commentService.createComment(commentCreateRequestDto, memberId);
            commentId = commentCreateResponseDto.getCommentId();
        } catch(CommentException exception) {
            Assertions.assertEquals(CommentErrorCode.COMMENT_REQUIRED, exception.getErrorCode());
        }


    }
}
