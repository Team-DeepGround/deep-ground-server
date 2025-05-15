package com.samsamhajo.deepground.qna.question.service;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.qna.question.Dto.QuestionRequestDto;
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

public class QuestionService{

    private final QuestionRepository questionRepository;
    //private final MemberRepository memberRepository;

    //질문 생성
    @Transactional
    public Long createQuestion(QuestionRequestDto questionRequestDto, Long memberId) {
//       Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        if(!StringUtils.hasText(questionRequestDto.getTitle())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_TITLE_NOT_FOUND);
        }

        if(!StringUtils.hasText(questionRequestDto.getContent())) {
            throw new QuestionException(QuestionErrorCode.QUESTION_CONTENT_NOT_FOUND);
        }

        //TODO : 미디어, 태그 처리 추후에 로직 작성


        Question question = Question.of(
                questionRequestDto.getTitle(),
                questionRequestDto.getContent(),
                null
        );

        return questionRepository.save(question).getId();
    }

}
