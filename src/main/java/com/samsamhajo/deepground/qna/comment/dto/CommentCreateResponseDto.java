package com.samsamhajo.deepground.qna.comment.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateResponseDto {

    private String commentContent;
    private Long answerId;
    private Long commentId;
    private Long memberId;

    public CommentCreateResponseDto( String commentContent, Long answerId, Long commentId, Long memberId) {
        this.commentContent = commentContent;
        this.answerId = answerId;
        this.commentId = commentId;
        this.memberId = memberId;
    }

    public static CommentCreateResponseDto of(String commentContent, Long answerId, Long commentId, Long memberId) {
        return new CommentCreateResponseDto(commentContent, answerId, commentId,  memberId);
    }
}