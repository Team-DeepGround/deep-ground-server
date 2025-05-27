package com.samsamhajo.deepground.qna.question.service;

import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.question.Dto.QuestionCreateRequestDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionUpdateResponseDto;
import com.samsamhajo.deepground.qna.question.Dto.QuestionUpdateRequestDto;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.exception.QuestionErrorCode;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionMediaRepository;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class QuestionService{

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final QuestionMediaService questionMediaService;
    private final QuestionMediaRepository questionMediaRepository;

    //질문 생성
    @Transactional
    public Question createQuestion(QuestionCreateRequestDto questionCreateRequestDto, Long memberId) {
//       Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        if(!StringUtils.hasText(questionCreateRequestDto.getTitle())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_TITLE_REQUIRED);
        }

        if(!StringUtils.hasText(questionCreateRequestDto.getContent())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_CONTENT_REQUIRED);
        }

        Question question = Question.of(
                questionCreateRequestDto.getTitle(),
                questionCreateRequestDto.getContent(),
                null
        );

        questionRepository.save(question);
        createQuestionMedia(questionCreateRequestDto, question);

        return question;
    }

    @Transactional
    public Long deleteQuestion(Long questionId, Long memberId) {

        //TODO : question을 작성한 멤버가 맞는지, 삭제권한 있는지 추후 로직 작성

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));

        questionMediaService.deleteQuestionMedia(questionId);
        questionRepository.deleteById(questionId);


        return question.getId();

    }

    public QuestionUpdateResponseDto updateQuestion(QuestionUpdateRequestDto questionUpdateRequestDto, Long memberId) {

//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        if(!StringUtils.hasText(questionUpdateRequestDto.getTitle())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_TITLE_REQUIRED);
        }

        if(!StringUtils.hasText(questionUpdateRequestDto.getContent())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_CONTENT_REQUIRED);
        }

        Question question = questionRepository.findById(questionUpdateRequestDto.getQuestionId())
                .orElseThrow(()-> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));

        QuestionUpdateResponseDto questionUpdateResponseDto = QuestionUpdateResponseDto.of(
                questionUpdateRequestDto.getQuestionId(),
                questionUpdateRequestDto.getTitle(),
                questionUpdateRequestDto.getContent(),
                null

        );

        question.updateQuesiton(questionUpdateRequestDto.getTitle(), questionUpdateRequestDto.getContent());

        questionMediaService.deleteQuestionMedia(question.getId());
        updateQuestionMedia(questionUpdateRequestDto, question);

        return questionUpdateResponseDto;

    }
    private void createQuestionMedia(QuestionCreateRequestDto questionCreateRequestDto, Question question) {
        questionMediaService.createQuestionMedia(question, questionCreateRequestDto.getImages());
    }

    private void updateQuestionMedia(QuestionUpdateRequestDto questionUpdateRequestDto, Question question) {
        questionMediaService.createQuestionMedia(question, questionUpdateRequestDto.getImages());
    }

}
