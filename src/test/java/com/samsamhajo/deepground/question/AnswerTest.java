package com.samsamhajo.deepground.question;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerUpdateRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerUpdateResponseDto;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.exception.AnswerException;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import com.samsamhajo.deepground.qna.answer.service.AnswerMediaService;
import com.samsamhajo.deepground.qna.answer.service.AnswerService;
import com.samsamhajo.deepground.qna.comment.entity.Comment;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.validation.CommonValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswerTest {

    @InjectMocks
    private AnswerService answerService;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CommonValidation commonValidation;

    @Mock
    private AnswerMediaService answerMediaService;

    private Question question;
    private Answer answer;
    private Member member;
    private Member member1;
    private Comment comment;

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
        //Member1
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(member, "publicId", UUID.randomUUID());
        //Member2
        ReflectionTestUtils.setField(member1, "id", 2L);
        ReflectionTestUtils.setField(member1, "publicId", UUID.randomUUID());
        //Question
        ReflectionTestUtils.setField(question, "id", 1L);
        //Answer
        ReflectionTestUtils.setField(answer, "id", 1L);
        //Comment
        ReflectionTestUtils.setField(comment, "id", 1L);
    }

    @Test
    @DisplayName("답변 작성 성공")
    public void answerWriteTest() {

        MockMultipartFile mockImg = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummy".getBytes());

        AnswerCreateRequestDto requestDto = new AnswerCreateRequestDto(answer.getAnswerContent(), List.of(mockImg) ,question.getId());

        when(commonValidation.QuestionValidation(question.getId())).thenReturn(question);
        when(memberRepository.getReferenceById(member.getId())).thenReturn(member);
        when(answerRepository.save(any(Answer.class))).thenReturn(answer);

        AnswerCreateResponseDto responseDto = answerService.createAnswer(requestDto, member.getId());

        /**
         * 작성한 answerId, answerContent, questionId, publicId 검증
         */
        assertThat(responseDto.getAnswerId()).isEqualTo(answer.getId());
        assertThat(responseDto.getAnswerContent()).isEqualTo(answer.getAnswerContent());
        assertThat(responseDto.getQuestionId()).isEqualTo(question.getId());
        assertThat(responseDto.getPublicId()).isEqualTo(member.getPublicId());
    }

    @Test
    @DisplayName("답변 작성 실패 : 답변 내용 미작성")
    public void answerWriteTest2() {
        String answerContent = "";

        MockMultipartFile mockImg = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummy".getBytes());
        AnswerCreateRequestDto requestDto = new AnswerCreateRequestDto(answerContent, List.of(mockImg) ,question.getId());


        assertThatThrownBy(() -> answerService.createAnswer(requestDto, member.getId()))
                .isInstanceOf(AnswerException.class)
                .hasMessageContaining("답변 내용을 찾을 수 없습니다.");

    }

    @Test
    @DisplayName("답변 수정 성공")
    public void answerModifyTest() {
        String answerContent = answer.getAnswerContent();
        String modifyContent = "수정된 내용";
        MockMultipartFile mockImg = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummy".getBytes());

        AnswerUpdateRequestDto requestDto = new AnswerUpdateRequestDto(modifyContent, question.getId(), answer.getId(), List.of(mockImg));

        List<String> mediaUrl = answerMediaService.createAnswerMedia(answer, List.of(mockImg));

        when(memberRepository.getReferenceById(member.getId())).thenReturn(member);
        when(commonValidation.AnswerValidation(answer.getId())).thenReturn(answer);

        AnswerUpdateResponseDto responseDto = answerService.updateAnswer(requestDto, member.getId());

        assertThat(responseDto.getAnswerContent()).isNotEqualTo(answerContent);
        assertThat(responseDto.getAnswerContent()).isEqualTo(modifyContent);
        assertThat(responseDto.getPublicId()).isEqualTo(member.getPublicId());
    }

    @Test
    @DisplayName("답변 수정 실패 : 작성자가 아닐 경우")
    public void answerModifyTest2() {
        String answerContent = answer.getAnswerContent();
        String modifyContent = "수정된 내용";

        MockMultipartFile mockImg = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummy".getBytes());

        AnswerUpdateRequestDto requestDto = new AnswerUpdateRequestDto(modifyContent, question.getId(), answer.getId(), List.of(mockImg));

        when(commonValidation.AnswerValidation(answer.getId())).thenReturn(answer);

        assertThatThrownBy(() -> answerService.updateAnswer(requestDto, member1.getId()))
                .isInstanceOf(AnswerException.class)
                        .hasMessageContaining("답변을 작성한 사용자가 아닙니다.");

    }

    @Test
    @DisplayName("답변 삭제 성공")
    void answerDeleteTest() {
        // given
        when(commonValidation.QuestionValidation(question.getId())).thenReturn(question);
        when(commonValidation.AnswerValidation(answer.getId())).thenReturn(answer);

        // when
        answerService.deleteAnswer(answer.getId(), member.getId(), question.getId());

        // then
        assertThat(answer.isDeleted()).isTrue();
    }


    @Test
    @DisplayName("답변 삭제 실패 : 작성자가 아닐 경우")
    public void answerDeleteTest2() {

        when(commonValidation.QuestionValidation(question.getId())).thenReturn(question);
        when(commonValidation.AnswerValidation(answer.getId())).thenReturn(answer);

        assertThatThrownBy(() -> answerService.deleteAnswer(answer.getId(), member1.getId(), question.getId()))
                .isInstanceOf(AnswerException.class)
                .hasMessageContaining("답변을 작성한 사용자가 아닙니다.");
    }

}
