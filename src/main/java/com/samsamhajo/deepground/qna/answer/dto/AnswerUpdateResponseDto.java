package com.samsamhajo.deepground.qna.answer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AnswerUpdateResponseDto {

    private String answerContent;
    private Long questionId;
    private Long answerId;
    private UUID publicId;
    private List<String> mediaUrl;

    public AnswerUpdateResponseDto(String answerContent, Long questionId, Long answerId, UUID publicId, List<String> mediaUrl) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.answerId = answerId;
        this.publicId = publicId;
        this.mediaUrl = mediaUrl;
    }

    public static AnswerUpdateResponseDto of(String answerContent, Long questionId, Long answerId, UUID publicId, List<String> mediaUrl) {
        return new AnswerUpdateResponseDto(answerContent, questionId, answerId, publicId, mediaUrl);
    }
}
