package com.samsamhajo.deepground.qna.comment.dto;

import com.samsamhajo.deepground.qna.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentDetail {

    Long commentId;
    Long answerId;
    Long memberId;
    String content;
    String nickname;

    public static CommentDetail of(Comment comment) {
        return new CommentDetail(
                comment.getId(),
                comment.getAnswer().getId(),
                comment.getMember().getId(),
                comment.getCommentContent(),
                comment.getMember().getNickname()
        );
    }
}
