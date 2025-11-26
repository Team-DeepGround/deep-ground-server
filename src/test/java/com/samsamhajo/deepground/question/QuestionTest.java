package com.samsamhajo.deepground.question;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.comment.entity.Comment;
import com.samsamhajo.deepground.qna.question.Dto.*;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionStatus;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.repository.QuestionTagRepository;
import com.samsamhajo.deepground.qna.question.service.QuestionMediaService;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import com.samsamhajo.deepground.qna.question.service.QuestionTagService;
import com.samsamhajo.deepground.qna.validation.CommonValidation;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CommonValidation commonValidation;

    @Mock
    private QuestionMediaService questionMediaService;

    @Mock
    private QuestionTagService questionTagService;

    @Mock
    private QuestionTagRepository questionTagRepository;

    @Mock
    private TechStackRepository techStackRepository;

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
    }

    @Test
    @DisplayName("질문 작성 성공")
    public void createQuestionTest() {

        MockMultipartFile mockImg = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummy".getBytes());
        List<String> techStacks = List.of("Java");

        QuestionCreateRequestDto requestDto = new QuestionCreateRequestDto(question.getTitle(), question.getContent(),techStacks, List.of(mockImg));

        when(commonValidation.MemberValidation(member.getId())).thenReturn(member);
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        QuestionCreateResponseDto responseDto = questionService.createQuestion(requestDto, member.getId());

        assertThat(responseDto.getTitle()).isEqualTo(question.getTitle());
        assertThat(responseDto.getContent()).isEqualTo(question.getContent());
        assertThat(responseDto.getTechStacks()).isEqualTo(techStacks);
        assertThat(responseDto.getPublicId()).isEqualTo(member.getPublicId());

    }

    @Test
    @DisplayName("질문 작성 실페 : 제목 없음")
    public void createQuestionFailedTest() {

        String title = "";

        MockMultipartFile mockImg = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummy".getBytes());
        List<String> techStacks = List.of("Java");

        QuestionCreateRequestDto requestDto = new QuestionCreateRequestDto(title, question.getContent() ,techStacks, List.of(mockImg));

        assertThatThrownBy(() -> questionService.createQuestion(requestDto, member.getId()))
                .isInstanceOf(QuestionException.class)
                .hasMessageContaining("제목을 찾을 수 없습니다.");

    }

    @Test
    @DisplayName("질문 작성 실페 : 내용 없음")
    public void createQuestionFailedTest2() {

        String content = "";

        MockMultipartFile mockImg = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummy".getBytes());
        List<String> techStacks = List.of("Java");

        QuestionCreateRequestDto requestDto = new QuestionCreateRequestDto(question.getTitle(), content ,techStacks, List.of(mockImg));

        assertThatThrownBy(() -> questionService.createQuestion(requestDto, member.getId()))
                .isInstanceOf(QuestionException.class)
                .hasMessageContaining("내용을 찾을 수 없습니다.");

    }

    @Test
    @DisplayName("질문 수정 성공")
    public void modifyQuestionTest() {

        String title = "원래 제목";
        String modifyTitle = "수정된 제목";

        String content = "원래 내용";
        String modifyContent = "수정된 내용";

        MockMultipartFile mockImg = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummy".getBytes());
        //Question 수정 시 들어가는 Java
        List<String> techStacks = List.of("Java");
        /**
         * Question TechStackRepo에 존재하는지를 검증하기 위해 만들어놓은 techStack
         * Mockito에서는 진짜 DB가 없기에 techStack을 선언한 후, findByName을 하면 이 객체를 반환해라라고 하지않으면
         * null값으로 들어가게 된다.
         */
        TechStack techStack = TechStack.of("Java", "Backend");

        QuestionUpdateRequestDto requestDto = new QuestionUpdateRequestDto(question.getId(),modifyTitle, modifyContent, techStacks, List.of(mockImg));

        when(commonValidation.QuestionValidation(question.getId())).thenReturn(question);
        when(techStackRepository.findByName("Java"))
                .thenReturn(Optional.of(techStack));

        QuestionUpdateResponseDto responseDto = questionService.updateQuestion(requestDto, member.getId());

        assertThat(responseDto.getTitle()).isNotEqualTo(title);
        assertThat(responseDto.getContent()).isNotEqualTo(content);
        assertThat(responseDto.getTechStacks()).isEqualTo(techStacks);
        assertThat(responseDto.getPublicId()).isEqualTo(member.getPublicId());
    }

    @Test
    @DisplayName("질문 수정 실페 : 작성자가 아닌 경우")
    public void modifyQuestionFailedTest() {

        String modifyTitle = "수정된 제목";

        String modifyContent = "수정된 내용";

        MockMultipartFile mockImg = new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "dummy".getBytes());
        //Question 수정 시 들어가는 Java
        List<String> techStacks = List.of("Java");

        QuestionUpdateRequestDto requestDto = new QuestionUpdateRequestDto(question.getId(), modifyTitle, modifyContent, techStacks, List.of(mockImg));

        when(commonValidation.QuestionValidation(question.getId())).thenReturn(question);

        assertThatThrownBy(() -> questionService.updateQuestion(requestDto, member1.getId()))
        .isInstanceOf(QuestionException.class)
                .hasMessageContaining("질문을 작성한 사용자가 아닙니다.");


    }

    @Test
    @DisplayName("질문 삭제 성공")
    public void deleteQuestionTest() {

        when(commonValidation.QuestionValidation(question.getId())).thenReturn(question);

        questionService.deleteQuestion(question.getId(), member.getId());

        assertThat(question.isDeleted()).isTrue();

    }

    @Test
    @DisplayName("질문 삭제 실패 : 작성자가 아닌 경우")
    public void deleteQuestionFailedTest() {

        when(commonValidation.QuestionValidation(question.getId())).thenReturn(question);

        assertThatThrownBy(() -> questionService.deleteQuestion(question.getId(), member1.getId()))
                .isInstanceOf(QuestionException.class)
                .hasMessageContaining("질문을 작성한 사용자가 아닙니다.");

    }

    @Test
    @DisplayName("질문 상태 변경 성공")
    public void questionStatusTest() {

        when(commonValidation.QuestionValidation(question.getId())).thenReturn(question);
        when(commonValidation.MemberValidation(member.getId())).thenReturn(member);

        QuestionStatus status = QuestionStatus.RESOLVED;

        QuestionUpdateStatusRequestDto requestDto = new QuestionUpdateStatusRequestDto(status);

        QuestionUpdateStatusResponseDto responseDto = questionService.updateQuestionStatus(requestDto, member.getId(), question.getId());

        assertThat(responseDto.getStatus()).isEqualTo(status);

    }

    @Test
    @DisplayName("질문 상태 변경 실패 : 작성자가 아닌 경우")
    public void questionStatusFailedTest() {

        when(commonValidation.QuestionValidation(question.getId())).thenReturn(question);
        when(commonValidation.MemberValidation(member1.getId())).thenReturn(member1);

        QuestionStatus status = QuestionStatus.RESOLVED;

        QuestionUpdateStatusRequestDto requestDto = new QuestionUpdateStatusRequestDto(status);

        assertThatThrownBy(() -> questionService.updateQuestionStatus(requestDto, member1.getId(), question.getId()))
                .isInstanceOf(QuestionException.class)
                .hasMessageContaining("질문을 작성한 사용자가 아닙니다.");
    }


}
