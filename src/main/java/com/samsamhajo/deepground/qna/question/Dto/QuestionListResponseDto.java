package com.samsamhajo.deepground.qna.question.Dto;

import lombok.Getter;

import java.util.List;

@Getter
public class QuestionListResponseDto {
    private List<QuestionSummaryDto> questions;
    private int totalPages;

    public static QuestionListResponseDto of(List<QuestionSummaryDto> questions, int totalPages) {
        return new QuestionListResponseDto(questions, totalPages);
    }

    private QuestionListResponseDto(List<QuestionSummaryDto> questions, int totalPages) {
        this.questions = questions;
        this.totalPages = totalPages;
    }
}

