package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.qna.question.entity.Question;
import lombok.Getter;

@Getter
public class QuestionResponseDto {

    private Long questionId;
    private String title;
    private String content;
    private Long memberId;

    public QuestionResponseDto(Long questionId, String title, String content, Long memberId) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
    public static QuestionResponseDto of(Long questionId, String title, String content, Long memberId) {
        return new QuestionResponseDto(questionId, title, content, memberId);
    }
}
