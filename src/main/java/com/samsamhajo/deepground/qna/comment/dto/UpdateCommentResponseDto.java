package com.samsamhajo.deepground.qna.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentResponseDto {

    private String commentContent;
    private UUID publicId;
    private Long answerId;
    private Long commentId;

    public static UpdateCommentResponseDto of(String commentContent, UUID publicId, Long answerId, Long commentId) {
        return new UpdateCommentResponseDto(commentContent, publicId, answerId, commentId);
    }

}

