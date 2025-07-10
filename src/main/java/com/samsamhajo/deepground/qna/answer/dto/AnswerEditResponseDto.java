package com.samsamhajo.deepground.qna.answer.dto;

import java.util.List;

public record AnswerEditResponseDto(
        Long answerId,
        Long questionId,
        String content,
        List<String> imageUrls
) {}
g