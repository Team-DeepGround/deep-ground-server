package com.samsamhajo.deepground.qna.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerCreateResponseDto {

    private Long questionId;
    private Long memberId;
    private String answerContent;



    public AnswerCreateResponseDto(String answerContent, Long questionId, Long memberId) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.memberId = memberId;
    }

    public static AnswerCreateResponseDto of(String answerContent, Long questionId, Long memberId) {
        return new AnswerCreateResponseDto(answerContent, questionId, memberId);
    }

}