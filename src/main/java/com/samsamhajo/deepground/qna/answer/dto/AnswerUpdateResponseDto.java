package com.samsamhajo.deepground.qna.answer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AnswerUpdateResponseDto {

    private String answerContent;
    private Long questionId;
    private Long answerId;
    private Long memberId;
    private List<String> mediaUrl;

    public AnswerUpdateResponseDto(String answerContent, Long questionId, Long answerId, Long memberId, List<String> mediaUrl) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.answerId = answerId;
        this.memberId = memberId;
        this.mediaUrl = mediaUrl;
    }

    public static AnswerUpdateResponseDto of(String answerContent, Long questionId, Long answerId, Long memberId, List<String> mediaUrl) {
        return new AnswerUpdateResponseDto(answerContent, questionId, answerId, memberId, mediaUrl);
    }
}
