package com.samsamhajo.deepground.qna.question.service;

import com.samsamhajo.deepground.qna.question.entity.Question;
import com.samsamhajo.deepground.qna.question.entity.QuestionTag;
import com.samsamhajo.deepground.qna.question.exception.QuestionErrorCode;
import com.samsamhajo.deepground.qna.question.exception.QuestionException;
import com.samsamhajo.deepground.qna.question.repository.QuestionRepository;
import com.samsamhajo.deepground.qna.question.repository.QuestionTagRepository;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionTagService {

    private final QuestionRepository questionRepository;
    private final QuestionTagRepository questionTagRepository;
    private final TechStackRepository techStackRepository;

    @Transactional
    public void TagList(Long questionId) {
        // 질문 찾기
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.QUESTION_NOT_FOUND));

        questionTagRepository.findAll().forEach(questionTag -> {
            System.out.println("Question ID: " + questionTag.getQuestion().getId());
            System.out.println("Tech Stack ID: " + questionTag.getTechStack().getId());
            System.out.println("Tech Stack Name: " + questionTag.getTechStack().getName());
            System.out.println("-------------------------");
        });

    }

}
