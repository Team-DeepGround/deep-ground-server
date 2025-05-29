package com.samsamhajo.deepground.qna.question.Dto;

import lombok.Getter;

@Getter
public class QuestionUpdateResponseDto {

    private Long questionId;
    private String title;
    private String content;
    private Long memberId;

    public QuestionUpdateResponseDto(Long questionId, String title, String content, Long memberId) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
    public static QuestionUpdateResponseDto of(Long questionId, String title, String content, Long memberId) {
        return new QuestionUpdateResponseDto(questionId, title, content, memberId);
    }
}
