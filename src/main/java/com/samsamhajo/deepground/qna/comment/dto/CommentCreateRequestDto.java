package com.samsamhajo.deepground.qna.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateRequestDto {
    @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    private String commentContent;
    private Long answerId;
}