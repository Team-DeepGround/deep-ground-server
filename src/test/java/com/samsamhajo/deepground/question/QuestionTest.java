package com.samsamhajo.deepground.qna.question;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.question.Dto.QuestionRequestDto;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class QuestionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QuestionService questionService;

    private Long memberId;

    @BeforeEach
    void 테스트용_멤버_저장() {
        // 테스트용 멤버 저장
        Member member = Member.createLocalMember("test@naver.com", "password123", "tester");
        memberRepository.save(member);
        memberId = member.getId();
    }

    @Test
    @DisplayName("질문 생성 통합 테스트 - 성공")
    void createQuestion_success() throws Exception {
        // given
        String title = "통합 테스트 질문 제목";
        String content = "이것은 질문 내용입니다.";

        MockMultipartFile mediaFile = new MockMultipartFile(
                "mediaFiles",
                "example.png",
                MediaType.IMAGE_PNG_VALUE,
                "dummy image content".getBytes()
        );

        // when & then
        String responseBody = mockMvc.perform(
                        multipart("/question")
                                .file(mediaFile)
                                .param("title", title)
                                .param("content", content)
                                .param("memberId", memberId.toString())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long questionId = jsonNode.get("result").get("id").asLong();

        // assert DB에 잘 들어갔는지 확인
        assertThat(questionRepository.findById(questionId)).isPresent();
        assertThat(questionRepository.findById(questionId).get().getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("질문 생성 성공 서비스 단위 테스트")
    void QuestionServiceTest() throws Exception {

        String title = "테스트";
        String content = "테스트1";

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        // techStack 예시 (빈 리스트 혹은 테스트용 값 넣기)
        List<Long> techStack = List.of(1L, 2L);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto(title, content, techStack, mediaFiles);

        Question question = questionService.createQuestion(questionRequestDto, memberId);

        assertThat(questionRepository.findById(question.getId())).isPresent();
        assertThat(questionRepository.findById(question.getId()).get().getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("질문 저장 및 조회 테스트")
    void QuestionRepositoryTest ()throws Exception {

        //given
        Question question = Question.of("테스트 제목", "테스트 내용", null);

        //when
        Question saved = questionRepository.save(question);
        Question found = questionRepository.findById(saved.getId()).orElseThrow();

        //then
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("테스트 제목");
    }

    @Test
    @DisplayName(("제목 없을 시 예외처리"))
    void QuestionTitle() throws Exception {

        String title = "";
        String content = "테스트1";

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        // techStack 예시 (빈 리스트 혹은 테스트용 값 넣기)
        List<Long> techStack = List.of(1L, 2L);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto(title, content, techStack, mediaFiles);

        assertThatThrownBy(() -> questionService.createQuestion(questionRequestDto, memberId)
        ).isInstanceOf(QuestionException.class)
                .hasMessageContaining("제목을 찾을 수 없습니다.");

    }

    @Test
    @DisplayName(("내용 없을 시 예외처리"))
    void QuestionContent() throws Exception {

        String title = "title";
        String content = "";

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        // techStack 예시 (빈 리스트 혹은 테스트용 값 넣기)
        List<Long> techStack = List.of(1L, 2L);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto(title, content, techStack, mediaFiles);

        assertThatThrownBy(() -> questionService.createQuestion(questionRequestDto, memberId)
        ).isInstanceOf(QuestionException.class)
                .hasMessageContaining("내용을 찾을 수 없습니다.");

    }

}
