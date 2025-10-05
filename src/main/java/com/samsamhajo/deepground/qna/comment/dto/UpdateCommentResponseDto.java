package com.samsamhajo.deepground.qna.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentResponseDto {

    private String commentContent;
    private Long memberId;
    private Long answerId;
    private Long commentId;

    public static UpdateCommentResponseDto of(String commentContent, Long memberId, Long answerId, Long commentId) {
        return new UpdateCommentResponseDto(commentContent, memberId, answerId, commentId);
    }

}

