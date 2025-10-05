package com.samsamhajo.deepground.qna.answer.dto;

import com.samsamhajo.deepground.qna.answer.entity.Answer;

import java.util.List;

public record AnswerEditResponseDto(
        Long answerId,
        Long questionId,
        String content,
        List<String> imageUrls
) {
    public static AnswerEditResponseDto of(Answer answer, List<String> imageUrls) {
        return new AnswerEditResponseDto(
                answer.getId(),
                answer.getQuestion().getId(),
                answer.getAnswerContent(),
                imageUrls
        );
    }
}
