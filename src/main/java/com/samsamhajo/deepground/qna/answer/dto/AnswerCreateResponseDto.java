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


    public AnswerCreateResponseDto(String answerContent, Long questionId, Long memberId, Long answerId, List<CommentDTO> comments, int likeCount) {
        this.answerContent = answerContent;
        this.questionId = questionId;
        this.memberId = memberId;
        this.answerId = answerId;
        this.comments = comments;
        this.likeCount = likeCount;
    }

    public static AnswerCreateResponseDto of(String answerContent, Long questionId, Long memberId, Long answerId, List<CommentDTO> comments, int likeCount) {
        return new AnswerCreateResponseDto(answerContent, questionId, memberId, answerId, comments, likeCount);
    }

}