package com.samsamhajo.deepground.qna.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCommentRequest {
    @NotBlank(message = "댓글 내용은 필수로 입력해야 합니다.")
    private String commentContent;
    private Long answerId;

    public static CreateCommentRequest of(String commentContent, Long answerId) {
        return new CreateCommentRequest(commentContent, answerId);
    }



}

