package com.samsamhajo.deepground.qna.question;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
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

        Long questionId = Long.parseLong(responseBody);

        // assert DB에 잘 들어갔는지 확인
        assertThat(questionRepository.findById(questionId)).isPresent();
        assertThat(questionRepository.findById(questionId).get().getTitle()).isEqualTo(title);
    }
}
