package com.samsamhajo.deepground;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerCreateResponseDto;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import com.samsamhajo.deepground.qna.answer.service.AnswerService;
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

@SpringBootTest
@Transactional
public class AnswerDeleteTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionService questionService;

    private Long memberId;

    @BeforeEach
    public void 회원_생성() {
        Member member = Member.createLocalMember("ds@gmail.com", "test", "test");
        memberRepository.save(member);
        memberId = member.getId();
    }

    @Test
    @DisplayName("답변 삭제 테스트")
    void deleteAnswer() {
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
        Long test1 = question.getId();

        //답변 생성
        AnswerCreateRequestDto answerCreateRequestDto = new AnswerCreateRequestDto(answerContent, mediaFiles, test1);
        AnswerCreateResponseDto answerCreateResponseDto = answerService.createAnswer(answerCreateRequestDto, memberId);
        System.out.println(answerCreateResponseDto.getAnswerContent());
        Long test2 = answerCreateResponseDto.getAnswerId();

        Long deleteId = answerService.deleteAnswer(test2, memberId, test1);

        assertThat(answerRepository.findById(test2).isEmpty()).isTrue();
        assertThat(deleteId.equals(test2)).isTrue();

        assertThat(answerRepository.findById(deleteId).isEmpty());
    }

}
