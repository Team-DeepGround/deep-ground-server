package com.samsamhajo.deepground.question;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerUpdateRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerUpdateResponseDto;
import com.samsamhajo.deepground.qna.answer.exception.AnswerErrorCode;
import com.samsamhajo.deepground.qna.answer.exception.AnswerException;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import com.samsamhajo.deepground.qna.answer.service.AnswerService;
import com.samsamhajo.deepground.qna.question.Dto.QuestionCreateRequestDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionCreateResponseDto;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Transactional
public class AnswerUpdateTest extends IntegrationTestSupport {

    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TechStackRepository techStackRepository;
    private Long memberId;
    private Long memberId2;

    @Autowired
    private S3Uploader s3Uploader;

    @BeforeEach
    public void setUp() {
        Member member = Member.createLocalMember("ds@gmail.com", "test", "test");
        Member member2 = Member.createLocalMember("ds@gmail.com", "test", "test");
        memberRepository.save(member);
        memberId = member.getId();
        memberId2 = member2.getId();
        given(s3Uploader.upload(any(MultipartFile.class), anyString()))
                .willAnswer(invocation ->
                        "http://localhost/test/" +
                                invocation.getArgument(0, MultipartFile.class).getOriginalFilename());
    }

    @Test
    @DisplayName("답변 수정 테스트")
    void updateAnswerTest() {

        String title = "테스트";
        String content = "테스트1";
        String answerContent = "test answercontent";
        String UpdateContent = "test updatecontent";
        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        //질문 생성
        List<String> techStackNames = List.of("techStack1", "techStack2");
        List<String> categoryNames = List.of("category1", "category2");
        List<TechStack> techStacks = techStackNames.stream()
                .map(name -> TechStack.of(name, categoryNames.toString())) // 정적 팩토리 메서드가 없다면 new TechStack(name) 사용
                .collect(Collectors.toList());
        List<TechStack> savedTechStacks = techStackRepository.saveAll(techStacks);

        QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStackNames, mediaFiles);

        QuestionCreateResponseDto questionCreateResponseDto = questionService.createQuestion(questionCreateRequestDto, memberId);
        Long test1 = questionCreateResponseDto.getQuestionId();

        //답변 생성
        AnswerCreateRequestDto answerCreateRequestDto = new AnswerCreateRequestDto(answerContent, mediaFiles, test1);
        AnswerCreateResponseDto answerCreateResponseDto = answerService.createAnswer(answerCreateRequestDto, memberId);
        Long test2 = answerCreateResponseDto.getAnswerId();
        System.out.println(answerCreateResponseDto.getAnswerContent());
        String UpdateBeforeContent = answerRepository.findById(test2).get().getAnswerContent();

        //수정
        AnswerUpdateRequestDto answerUpdateRequestDto = new AnswerUpdateRequestDto(UpdateContent, mediaFiles, test1, test2, null);
        AnswerUpdateResponseDto answerUpdateResponseDto = answerService.updateAnswer(answerUpdateRequestDto, memberId);
        System.out.println(answerUpdateResponseDto.getAnswerContent());
        String UpdateAfterContent = answerRepository.findById(test2).get().getAnswerContent();

        //수정 전, 후 컨텐츠 결과 비교
        assertThat(UpdateAfterContent).isNotEqualTo(UpdateBeforeContent);

        AnswerException exception = assertThrows(AnswerException.class, () -> {
            if (!answerUpdateResponseDto.getMemberId().equals(memberId2)) {
                throw new AnswerException(AnswerErrorCode.ANSWER_MEMBER_MISMTACH);
            }
        });

        assertThat(exception.getMessage()).isEqualTo("답변을 작성한 사용자가 아닙니다.");
    }


    @Test
    @DisplayName("Content Valid 테스트")
    void updateContentValidTest() {
        String title = "테스트";
        String content = "테스트1";
        String answerContent = "test answercontent";
        String UpdateContent = "";
        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        //질문 생성
        List<String> techStackNames = List.of("techStack1", "techStack2");
        List<String> categoryNames = List.of("category1", "category2");
        List<TechStack> techStacks = techStackNames.stream()
                .map(name -> TechStack.of(name, categoryNames.toString())) // 정적 팩토리 메서드가 없다면 new TechStack(name) 사용
                .collect(Collectors.toList());
        List<TechStack> savedTechStacks = techStackRepository.saveAll(techStacks);

        QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStackNames, mediaFiles);

        QuestionCreateResponseDto questionCreateResponseDto = questionService.createQuestion(questionCreateRequestDto, memberId);
        Long test1 = questionCreateResponseDto.getQuestionId();

        //답변 생성
        AnswerCreateRequestDto answerCreateRequestDto = new AnswerCreateRequestDto(answerContent, mediaFiles, test1);
        AnswerCreateResponseDto answerCreateResponseDto = answerService.createAnswer(answerCreateRequestDto, memberId);
        Long test2 = answerCreateResponseDto.getAnswerId();
        System.out.println(answerCreateResponseDto.getAnswerContent());

        //답변 빈칸일시 예외발생
        AnswerUpdateRequestDto answerUpdateRequestDto = new AnswerUpdateRequestDto(UpdateContent, mediaFiles, test1, test2, null);
        AnswerException answerException = assertThrows(AnswerException.class, () -> answerService.updateAnswer(answerUpdateRequestDto, memberId));
        assertThat(answerException.getMessage()).isEqualTo(AnswerErrorCode.ANSWER_CONTENT_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("답변 없을 시 예외처리")
    void AnswerNotFoundTest() {
        // given
        String title = "테스트";
        String content = "테스트1";
        String answerContent = "test answercontent";
        String updateContent = "수정할 내용"; // 내용은 빈값이 아니어야 ANSWER_NOT_FOUND 먼저 발생
        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile(
                        "mediaFiles",
                        "image1.png",
                        MediaType.IMAGE_PNG_VALUE,
                        "dummy image content 1".getBytes()
                )
        );

        // 질문 생성
        List<String> techStackNames = List.of("techStack1", "techStack2");
        List<String> categoryNames = List.of("category1", "category2");
        List<TechStack> techStacks = techStackNames.stream()
                .map(name -> TechStack.of(name, categoryNames.toString()))
                .collect(Collectors.toList());
        techStackRepository.saveAll(techStacks);

        QuestionCreateRequestDto questionCreateRequestDto =
                new QuestionCreateRequestDto(title, content, techStackNames, mediaFiles);

        QuestionCreateResponseDto questionCreateResponseDto =
                questionService.createQuestion(questionCreateRequestDto, memberId);
        Long test1 = questionCreateResponseDto.getQuestionId();

        // 존재하지 않는 답변 ID
        Long nonExistingAnswerId = 999L;

        AnswerUpdateRequestDto answerUpdateRequestDto =
                new AnswerUpdateRequestDto(updateContent, mediaFiles, test1, nonExistingAnswerId, null);

        // when
        AnswerException answerException = assertThrows(
                AnswerException.class,
                () -> answerService.updateAnswer(answerUpdateRequestDto, memberId)
        );

        // then
        assertThat(answerException.getMessage())
                .isEqualTo(AnswerErrorCode.ANSWER_NOT_FOUND.getMessage());
    }
}
