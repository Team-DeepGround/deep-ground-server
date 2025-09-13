package com.samsamhajo.deepground.question;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
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
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class AnswerDeleteTest extends IntegrationTestSupport {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TechStackRepository techStackRepository;

    @Autowired
    private S3Uploader s3Uploader;


    private Long memberId;
    private Long memberId2;

    @BeforeEach
    public void 회원_생성() {
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
    @DisplayName("답변 삭제 테스트")
    void deleteAnswer() {
        String title = "테스트";
        String content = "테스트1";
        String answerContent = "test answercontent";
        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png",
                        MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
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
        Long questionId = questionCreateResponseDto.getQuestionId();

        // 답변 생성
        AnswerCreateRequestDto answerCreateRequestDto =
                new AnswerCreateRequestDto(answerContent, mediaFiles, questionId);
        AnswerCreateResponseDto answerCreateResponseDto =
                answerService.createAnswer(answerCreateRequestDto, memberId);
        Long answerId = answerCreateResponseDto.getAnswerId();

        // 답변 삭제
        Long deleteId = answerService.deleteAnswer(answerId, memberId);

        assertThat(answerRepository.findById(answerId).isEmpty()).isTrue();
        assertThat(deleteId.equals(answerId)).isTrue();

        // 작성자 불일치 예외 테스트
        AnswerException exception = assertThrows(AnswerException.class, () -> {
            if (!answerCreateResponseDto.getMemberId().equals(memberId2)) {
                throw new AnswerException(AnswerErrorCode.ANSWER_MEMBER_MISMTACH);
            }
        });

        assertThat(exception.getMessage()).isEqualTo("답변을 작성한 사용자가 아닙니다.");
        System.out.println(exception.getMessage());
    }
}
