package com.samsamhajo.deepground.qna.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCommentResponseDto {

    private String commentContent;
    private Long memberId;
    private Long answerId;
    private Long commentId;

    public UpdateCommentResponseDto (String commentContent, Long memberId, Long answerId, Long commentId) {
        this.commentContent = commentContent;
        this.memberId = memberId;
        this.answerId = answerId;
        this.commentId = commentId;
    }

    public static UpdateCommentResponseDto of(String commentContent, Long memberId, Long answerId, Long commentId) {
        return new UpdateCommentResponseDto(commentContent, memberId, answerId, commentId);
    }
}
