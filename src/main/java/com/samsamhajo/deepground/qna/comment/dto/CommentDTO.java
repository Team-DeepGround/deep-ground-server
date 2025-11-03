package com.samsamhajo.deepground.qna.comment.dto;

import lombok.Getter;

import java.util.UUID;


@Getter
public class CommentDTO {
    private Long commentId;
    private String content;
    private UUID publicId;
    private String nickName;

    public CommentDTO(Long commentId, String content, UUID publicId, String nickName) {
        this.commentId = commentId;
        this.content = content;
        this.publicId = publicId;
        this.nickName = nickName;
    }

    public static CommentDTO of(Long commentId, String content, UUID publicId, String nickname) {
        return new CommentDTO(commentId, content, publicId, nickname);
    }
}

