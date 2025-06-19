package com.samsamhajo.deepground.qna.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AnswerDetailDto {

    private String answerContent;
    private Long questionId;
    private Long memberId;
    private Long answerId;
    private List<String> mediaUrls;

    public AnswerDetailDto(
            String answerContent,
            Long questionId,
            Long memberId,
            Long answerId,
            List<String> mediaUrls

    ) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.memberId = memberId;
        this.answerId = answerId;
        this.mediaUrls = mediaUrls;
    }

    public static AnswerDetailDto of(String answerContent, Long questionId, Long memberId, Long answerId, List<String> mediaUrls) {
        return new AnswerDetailDto(answerContent, questionId, memberId, answerId, mediaUrls);
    }
}
