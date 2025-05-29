package com.samsamhajo.deepground;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.question.Dto.QuestionCreateRequestDto;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import com.samsamhajo.deepground.qna.question.repository.QuestionMediaRepository;
import com.samsamhajo.deepground.qna.question.service.QuestionMediaService;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class QuestionMediaTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMediaRepository questionMediaRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long memberId;
    @Autowired
    private QuestionMediaService questionMediaService;

    @BeforeEach
    void 테스트용_멤버_저장() {
        // 테스트용 멤버 저장
        Member member = Member.createLocalMember("tses1@gmail.com", "TSE S1", "TSE S2");
        memberRepository.save(member);
        memberId = member.getId();
    }

    @Test
    @DisplayName("질문_미디어_생성_후 삭제_확인_테스트")
    void question_exception(){

        String title = "미디어 생성 테스트";
        String content = "미디어 생성 테스트 답변";

        List<Long> techStack = List.of(1L, 2L);

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        //RequestDto에 인자 전달 후, Question 생성
        QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStack, mediaFiles);
        Question question = questionService.createQuestion(questionCreateRequestDto, memberId);

        List<QuestionMedia> mediaList = List.of(
                        new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
                ).stream()
                .map(file -> QuestionMedia.of(file.getOriginalFilename(), "dummy-url", question))
                .collect(Collectors.toList());

        questionMediaRepository.saveAll(mediaList);
        questionMediaService.deleteQuestionMedia(question.getId());

        assertThat(questionMediaRepository.findAllByQuestionId(question.getId()).isEmpty()).isTrue();


    }
}
