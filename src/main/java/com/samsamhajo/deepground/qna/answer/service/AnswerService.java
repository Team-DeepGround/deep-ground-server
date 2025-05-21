package com.samsamhajo.deepground.qna.answer.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.answer.dto.AnswerRequestDto;
import com.samsamhajo.deepground.qna.answer.dto.AnswerResponseDto;
import com.samsamhajo.deepground.qna.answer.entity.Answer;
import com.samsamhajo.deepground.qna.answer.repository.AnswerRepository;
import com.samsamhajo.deepground.qna.answer.exception.AnswerErrorCode;
import com.samsamhajo.deepground.qna.answer.exception.AnswerException;
import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.exception.QuestionErrorCode;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public AnswerResponseDto createAnswer(AnswerRequestDto answerRequestDto , Long memberId, Long questionId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));

        if(!StringUtils.hasText(answerRequestDto.getAnswerContent())) {
            throw new AnswerException(AnswerErrorCode.ANSWER_CONTENT_REQUIRED);
        }

        Answer answer = Answer.of(
                answerRequestDto.getAnswerContent(),
                member,
                question
        );

        Answer saved = answerRepository.save(answer);

        question.incrementAnswerCount();

        return new AnswerResponseDto(saved.getId(), saved.getAnswerContent(), saved.getQuestion().getId(), saved.getMember().getId());
    }
}
