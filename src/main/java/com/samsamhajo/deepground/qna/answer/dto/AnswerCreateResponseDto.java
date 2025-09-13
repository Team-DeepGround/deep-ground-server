package com.samsamhajo.deepground.qna.answer.dto;

import com.samsamhajo.deepground.qna.comment.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AnswerCreateResponseDto {

    private Long questionId;
    private Long memberId;
    private String answerContent;
    private Long answerId;
    private List<CommentDTO> comments;
    private int likeCount;
    private List<String> mediaUrls;
    private LocalDateTime createdAt;
    private String nickname;


    public AnswerCreateResponseDto(String answerContent, Long questionId, Long memberId, Long answerId, List<CommentDTO> comments, int likeCount, List<String> mediaUrls, LocalDateTime createdAt, String nickname) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.memberId = memberId;
        this.answerId = answerId;
        this.comments = comments;
        this.likeCount = likeCount;
        this.mediaUrls = mediaUrls;
        this.createdAt = createdAt;
        this.nickname = nickname;
    }

    public static AnswerCreateResponseDto of(String answerContent, Long questionId, Long memberId, Long answerId, List<CommentDTO> comments, int likeCount, List<String> mediaUrls, LocalDateTime createdAt, String nickname) {
        return new AnswerCreateResponseDto(answerContent, questionId, memberId, answerId, comments, likeCount, mediaUrls, createdAt, nickname);
    }

}