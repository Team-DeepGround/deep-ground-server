package com.samsamhajo.deepground.qna.comment.dto;

import lombok.Getter;


@Getter
public class CommentDTO {
    private Long commentId;
    private String content;
    private Long memberId;
    private String nickName;

    public CommentDTO(Long commentId, String content, Long memberId, String nickName) {
        this.commentId = commentId;
        this.content = content;
        this.memberId = memberId;
        this.nickName = nickName;
    }

    public static CommentDTO of(Long commentId, String content, Long memberId, String nickname) {
        return new CommentDTO(commentId, content, memberId, nickname);
    }
}

