package com.samsamhajo.deepground.question;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.question.Dto.QuestionRequestDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionResponseDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionUpdateDto;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QuestionUpdateTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MemberRepository memberRepository;

    private Long memberId;

    @BeforeEach
    void 테스트용_멤버_저장() {
        // 테스트용 멤버 저장
        Member member = Member.createLocalMember("test@naver.com", "password123", "tester");
        memberRepository.save(member);
        memberId = member.getId();
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

        List<Long> techStack = List.of(1L, 2L);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto(title, content, techStack, mediaFiles);
        Question question = questionService.createQuestion(questionRequestDto, memberId);
        QuestionUpdateDto questionUpdateDto =  new QuestionUpdateDto(question.getId(), title1, content1, techStack, mediaFiles);
        QuestionResponseDto questionResponseDto = questionService.updateQuestion(questionUpdateDto, memberId);

        //TODO 후에 media, teckStack함께 수정되는지 Test코드 작성

        //MemberId가 서로 동일한지, 동일한 회원이 자신이 작성한 질문을 수정하는지
        assertThat(question.getMember()).isEqualTo(questionResponseDto.getMemberId());
        //같은 Question이 수정되고 있는지
        assertThat(question.getId()).isEqualTo(questionUpdateDto.getQuestionId());
        //제목이 수정되었는지
        assertThat(question.getTitle()).isEqualTo(questionUpdateDto.getTitle());
        //내용이 수정되었는지
        assertThat(question.getContent()).isEqualTo(questionUpdateDto.getContent());
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

        List<Long> techStack = List.of(1L, 2L);

        //TODO 후에 media, teckStack함께 수정되는지 Test코드 작성

        QuestionRequestDto questionRequestDto = new QuestionRequestDto(title, content, techStack, mediaFiles);
        Question question = questionService.createQuestion(questionRequestDto, memberId);
        QuestionUpdateDto questionUpdateDto =  new QuestionUpdateDto(question.getId(), title1, content1, techStack, mediaFiles);
        QuestionResponseDto questionResponseDto = questionService.updateQuestion(questionUpdateDto, memberId);

        assertThat(question.getMember()).isEqualTo(questionResponseDto.getMemberId());
        assertThat(questionRepository.findById(questionUpdateDto.getQuestionId()).get().getTitle()).isEqualTo(questionUpdateDto.getTitle());
        assertThat(questionRepository.findById(questionUpdateDto.getQuestionId()).get().getContent()).isEqualTo(questionUpdateDto.getContent());

    }

    @Test
    @DisplayName("수정된 제목이 없을 시 예외처리")
    public void testUpdateQuestion3() {
        String title = "테스트";
        String content = "테스트1";

        String title1 = "";
        String content1 = "수정된 테스트 내용";

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        List<Long> techStack = List.of(1L, 2L);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto(title, content, techStack, mediaFiles);
        Question question = questionService.createQuestion(questionRequestDto, memberId);
        QuestionUpdateDto questionUpdateDto =  new QuestionUpdateDto(question.getId(), title1, content1, techStack, mediaFiles);

        assertThatThrownBy(() -> questionService.updateQuestion(questionUpdateDto, memberId)
        ).isInstanceOf(QuestionException.class)
                .hasMessageContaining("제목을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("수정된 내용이 없을 시 예외처리")
    public void testUpdateQuestion4() {
        String title = "테스트";
        String content = "테스트1";

        String title1 = "수정된 테스트 제목";
        String content1 = "";

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        List<Long> techStack = List.of(1L, 2L);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto(title, content, techStack, mediaFiles);
        Question question = questionService.createQuestion(questionRequestDto, memberId);
        QuestionUpdateDto questionUpdateDto =  new QuestionUpdateDto(question.getId(), title1, content1, techStack, mediaFiles);

        assertThatThrownBy(() -> questionService.updateQuestion(questionUpdateDto, memberId)
        ).isInstanceOf(QuestionException.class)
                .hasMessageContaining("내용을 찾을 수 없습니다.");
    }

}
