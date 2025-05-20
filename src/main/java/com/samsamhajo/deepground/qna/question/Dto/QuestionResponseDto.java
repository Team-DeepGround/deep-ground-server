package com.samsamhajo.deepground.qna.question.Dto;

import com.samsamhajo.deepground.qna.question.entity.Question;
import lombok.Getter;

@Getter
public class QuestionResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long memberId;

    public QuestionResponseDto(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.memberId = question.getMember() != null ? question.getMember().getId() : null;
    }}
