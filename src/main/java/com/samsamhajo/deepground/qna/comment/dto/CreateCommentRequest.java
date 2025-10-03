package com.samsamhajo.deepground.qna.comment.dto;

import com.samsamhajo.deepground.qna.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    @NotBlank(message = "댓글 내용은 필수로 입력해야 합니다.")
    private String commentContent;
    private Long answerId;



}

