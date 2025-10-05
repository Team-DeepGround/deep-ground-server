package com.samsamhajo.deepground.qna.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateCommentRequestDto {

    @NotBlank(message = "댓글은 필수로 입력해야 합니다.")
    private String commentContent;
    private Long answerId;
    private Long commentId;

}

