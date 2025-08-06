package com.samsamhajo.deepground.question;

import com.samsamhajo.deepground.IntegrationTestSupport;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.question.Dto.QuestionCreateRequestDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionCreateResponseDto;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
import com.samsamhajo.deepground.qna.question.repository.QuestionMediaRepository;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.service.QuestionMediaService;
import com.samsamhajo.deepground.qna.question.service.QuestionService;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
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

@Transactional
public class QuestionMediaTest extends IntegrationTestSupport {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMediaRepository questionMediaRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long memberId;
    @Autowired
    private QuestionMediaService questionMediaService;
    @Autowired
    private TechStackRepository techStackRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문_미디어_생성_후 삭제_확인_테스트")
    void question_exception(){

        Member member = Member.createLocalMember("tses1@gmail.com", "TSE S1", "TSE S2");
        memberRepository.save(member);
        memberId = member.getId();

        String title = "미디어 생성 테스트";
        String content = "미디어 생성 테스트 답변";

        List<MultipartFile> mediaFiles = List.of(
                new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
        );

        //RequestDto에 인자 전달 후, Question 생성
        List<String> techStackNames = List.of("techStack1", "techStack2");
        List<String> categoryNames = List.of("category1", "category2");
        List<TechStack> techStacks = techStackNames.stream()
                .map(name -> TechStack.of(name, categoryNames.toString())) // 정적 팩토리 메서드가 없다면 new TechStack(name) 사용
                .collect(Collectors.toList());
        List<TechStack> savedTechStacks = techStackRepository.saveAll(techStacks);

        QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto(title, content, techStackNames, mediaFiles);
        QuestionCreateResponseDto questionCreateResponseDto = questionService.createQuestion(questionCreateRequestDto, memberId);

            Question question = questionRepository.save(Question.of(
                title,
                content,
                member
        ));

        List<QuestionMedia> mediaList = List.of(
                        new MockMultipartFile("mediaFiles", "image1.png", MediaType.IMAGE_PNG_VALUE, "dummy image content 1".getBytes())
                ).stream()
                .map(file -> QuestionMedia.of(file.getOriginalFilename(), "dummy-url", question))
                .collect(Collectors.toList());

        questionMediaRepository.saveAll(mediaList);
        questionMediaService.deleteQuestionMedia(questionCreateResponseDto.getQuestionId());

        assertThat(questionMediaRepository.findAllByQuestionId(questionCreateResponseDto.getQuestionId()).isEmpty()).isTrue();


    }
}
