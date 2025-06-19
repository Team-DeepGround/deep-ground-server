package com.samsamhajo.deepground.qna.answer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerUpdateResponseDto {

    private String answerContent;
    private Long questionId;
    private Long answerId;
    private Long memberId;

    public AnswerUpdateResponseDto(String answerContent, Long questionId, Long answerId, Long memberId) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.answerId = answerId;
        this.memberId = memberId;
    }

    public static AnswerUpdateResponseDto of(String answerContent, Long questionId, Long answerId, Long memberId) {
        return new AnswerUpdateResponseDto(answerContent, questionId, answerId, memberId);
    }
}
