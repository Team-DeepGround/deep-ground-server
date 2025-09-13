package com.samsamhajo.deepground.qna.answer.dto;

import com.samsamhajo.deepground.qna.comment.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AnswerDetailDto {

    private String answerContent;
    private Long questionId;
    private Long memberId;
    private Long answerId;
    private List<String> mediaUrl;
    private LocalDateTime createdAt;

    public AnswerDetailDto(
            String answerContent,
            Long questionId,
            Long memberId,
            Long answerId,
            List<String> mediaUrl,
            LocalDateTime createdAt

    ) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.memberId = memberId;
        this.answerId = answerId;
        this.mediaUrl = mediaUrl;
        this.createdAt = createdAt;
    }

    public static AnswerDetailDto of(String answerContent, Long questionId, Long memberId, Long answerId, List<String> mediaUrl, LocalDateTime createdAt) {
        return new AnswerDetailDto(answerContent, questionId, memberId, answerId, mediaUrl, createdAt);
    }
}
