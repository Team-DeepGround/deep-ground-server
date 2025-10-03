package com.samsamhajo.deepground.qna.comment.dto;


import com.samsamhajo.deepground.qna.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateResponse {

    private String commentContent;
    private Long answerId;
    private Long commentId;
    private Long memberId;

    public static CommentCreateResponse changeEntity(Comment comment) {
        return new CommentCreateResponse(
                comment.getCommentContent(),
                comment.getAnswer().getId(),
                comment.getId(),
                comment.getMember().getId()
        );
    }

}
