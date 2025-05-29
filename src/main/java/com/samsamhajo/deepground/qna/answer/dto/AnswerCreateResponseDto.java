package com.samsamhajo.deepground.qna.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerCreateResponseDto {

    private Long questionId;
    private Long memberId;
    private String answerContent;
    private Long answerId;


    public AnswerCreateResponseDto(String answerContent, Long questionId, Long memberId, Long answerId) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.memberId = memberId;
        this.answerId = answerId;
    }

    public static AnswerCreateResponseDto of(String answerContent, Long questionId, Long memberId, Long answerId) {
        return new AnswerCreateResponseDto(answerContent, questionId, memberId, answerId);
    }

}