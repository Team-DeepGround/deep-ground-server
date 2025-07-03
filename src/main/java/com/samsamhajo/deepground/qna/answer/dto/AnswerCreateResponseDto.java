package com.samsamhajo.deepground.qna.answer.dto;

import com.samsamhajo.deepground.qna.comment.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

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


    public AnswerCreateResponseDto(String answerContent, Long questionId, Long memberId, Long answerId, List<CommentDTO> comments, int likeCount, List<String> mediaUrls) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.memberId = memberId;
        this.answerId = answerId;
        this.comments = comments;
        this.likeCount = likeCount;
        this.mediaUrls = mediaUrls;
    }

    public static AnswerCreateResponseDto of(String answerContent, Long questionId, Long memberId, Long answerId, List<CommentDTO> comments, int likeCount, List<String> mediaUrls) {
        return new AnswerCreateResponseDto(answerContent, questionId, memberId, answerId, comments, likeCount, mediaUrls);
    }

}