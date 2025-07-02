package com.samsamhajo.deepground.qna.answer.dto;

import com.samsamhajo.deepground.qna.comment.dto.CommentDTO;
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
    private List<String> mediaUrl;

    public AnswerDetailDto(
            String answerContent,
            Long questionId,
            Long memberId,
            Long answerId,
            List<String> mediaUrl

    ) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.memberId = memberId;
        this.answerId = answerId;
        this.mediaUrl = mediaUrl;
    }

    public static AnswerDetailDto of(String answerContent, Long questionId, Long memberId, Long answerId, List<String> mediaUrl) {
        return new AnswerDetailDto(answerContent, questionId, memberId, answerId, mediaUrl);
    }
}
