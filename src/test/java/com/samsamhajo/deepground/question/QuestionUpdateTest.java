package com.samsamhajo.deepground.question;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.question.Dto.QuestionCreateRequestDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionCreateResponseDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionUpdateResponseDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionUpdateRequestDto;
import com.samsamhajo.deepground.qna.question.exception.QuestionErrorCode;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Transactional
@ExtendWith(SpringExtension.class)
public class QuestionUpdateTest extends IntegrationTestSupport {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MemberRepository memberRepository;

    private Long memberId;
    private Long memberId2;
    @Autowired
    private TechStackRepository techStackRepository;
    @Autowired
    private S3Uploader s3Uploader;

    @BeforeEach
    void 테스트용_멤버_저장() {
        // 테스트용 멤버 저장
        Member member = Member.createLocalMember("test@naver.com", "password123", "tester");
        Member member2 = Member.createLocalMember("test@naver.com", "password123", "tester");
        memberRepository.save(member);

        memberId = member.getId();
        memberId2 = member2.getId();
        given(s3Uploader.upload(any(MultipartFile.class), anyString()))
                .willAnswer(invocation ->
                        "http://localhost/test/" +
                                invocation.getArgument(0, MultipartFile.class).getOriginalFilename());
    }

    @Test
    @DisplayName("질문 수정 Service 테스트")
    public void testUpdateQuestion() {

        String title = "테스트";
        String content = "테스트1";

        String title1 = "수정된 테스트 제목";
        String content1 = "수정된 테스트 내용";

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        List<String> techStackNames = List.of("techStack1", "techStack2");
        List<String> categoryNames = List.of("category1", "category2");
        List<TechStack> techStacks = techStackNames.stream()
                .map(name -> TechStack.of(name, categoryNames.toString())) // 정적 팩토리 메서드가 없다면 new TechStack(name) 사용
                .collect(Collectors.toList());
        List<TechStack> savedTechStacks = techStackRepository.saveAll(techStacks);

        QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStackNames, mediaFiles);
        QuestionCreateResponseDto questionCreateResponseDto = questionService.createQuestion(questionCreateRequestDto, memberId);
        QuestionUpdateRequestDto questionUpdateRequestDto =  new QuestionUpdateRequestDto(questionCreateResponseDto.getQuestionId(), title1, content1, techStackNames, mediaFiles);
        QuestionUpdateResponseDto questionUpdateResponseDto = questionService.updateQuestion(questionUpdateRequestDto, memberId);

        //TODO 후에 media, teckStack함께 수정되는지 Test코드 작성

        //MemberId가 서로 동일한지, 동일한 회원이 자신이 작성한 질문을 수정하는지
        assertThat(questionCreateResponseDto.getMemberId()).isEqualTo(questionUpdateResponseDto.getMemberId());
        //같은 Question이 수정되고 있는지
        assertThat(questionCreateResponseDto.getQuestionId()).isEqualTo(questionUpdateRequestDto.getQuestionId());
        //제목이 수정되었는지
        assertThat(questionUpdateResponseDto.getTitle()).isEqualTo(title1);
        //내용이 수정되었는지
        assertThat(questionUpdateResponseDto.getContent()).isEqualTo(content1);
        System.out.println(memberId);
        System.out.println(memberId2);
        //질문을 쓴 사용자만 업데이트가 가능한지
        QuestionException exception = assertThrows(QuestionException.class, () -> {
            if(!questionUpdateResponseDto.getMemberId().equals(memberId2)) {
                throw new QuestionException(QuestionErrorCode.QUESTION_MEMBER_MISMATCH);
            }
        });
        assertThat(exception.getMessage()).isEqualTo("질문을 작성한 사용자가 아닙니다.");
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("질문 수정 후 DB에 반영되는지 확인 테스트")
    public void testUpdateQuestion2() {

        String title = "테스트";
        String content = "테스트1";

        String title1 = "수정된 테스트 제목";
        String content1 = "수정된 테스트 내용";

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        List<String> techStackNames = List.of("techStack1", "techStack2");
        List<String> categoryNames = List.of("category1", "category2");
        List<TechStack> techStacks = techStackNames.stream()
                .map(name -> TechStack.of(name, categoryNames.toString())) // 정적 팩토리 메서드가 없다면 new TechStack(name) 사용
                .collect(Collectors.toList());
        List<TechStack> savedTechStacks = techStackRepository.saveAll(techStacks);


        QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStackNames, mediaFiles);
        QuestionCreateResponseDto questionCreateResponseDto = questionService.createQuestion(questionCreateRequestDto, memberId);
        QuestionUpdateRequestDto questionUpdateRequestDto =  new QuestionUpdateRequestDto(questionCreateResponseDto.getQuestionId(), title1, content1, techStackNames, mediaFiles);
        QuestionUpdateResponseDto questionUpdateResponseDto = questionService.updateQuestion(questionUpdateRequestDto, memberId);

        assertThat(questionCreateResponseDto.getMemberId()).isEqualTo(questionUpdateResponseDto.getMemberId());
        assertThat(questionRepository.findById(questionUpdateRequestDto.getQuestionId()).get().getTitle()).isEqualTo(questionUpdateRequestDto.getTitle());
        assertThat(questionRepository.findById(questionUpdateRequestDto.getQuestionId()).get().getContent()).isEqualTo(questionUpdateRequestDto.getContent());

    }

}
