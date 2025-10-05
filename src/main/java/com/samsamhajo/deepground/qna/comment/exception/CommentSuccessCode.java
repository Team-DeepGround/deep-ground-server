package com.samsamhajo.deepground.qna.comment.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentSuccessCode implements SuccessCode {

    COMMENT_CREATED(HttpStatus.CREATED, "댓글이 성공적으로 생성되었습니다."),
    COMMENT_UPDATED(HttpStatus.OK, "댓글이 성공적으로 수정되었습니다."),
    COMMENT_DELETED(HttpStatus.OK, "댓글이 성공적으로 삭제되었습니다."),
    COMMENT_SUCCESS_SEARCH(HttpStatus.OK, "댓글들이 성공적으로 조회되었습니다.");

    private final HttpStatus status;
    private final String message;

    CommentSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}

