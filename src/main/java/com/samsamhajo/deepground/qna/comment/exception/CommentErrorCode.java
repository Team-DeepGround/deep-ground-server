package com.samsamhajo.deepground.qna.comment.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentErrorCode implements ErrorCode {

    COMMENT_REQUIRED(HttpStatus.BAD_GATEWAY, "댓글은 필수입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 없습니다."),
    COMMENT_ANSWER_MISMATCH(HttpStatus.BAD_REQUEST, "답변에 속한 댓글이 아닙니다."),
    COMMENT_MEMBER_MISMATCH(HttpStatus.BAD_REQUEST, "댓글을 작성한 멤버가 아닙니다.");


    private final HttpStatus status;
    private final String message;

    CommentErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}

