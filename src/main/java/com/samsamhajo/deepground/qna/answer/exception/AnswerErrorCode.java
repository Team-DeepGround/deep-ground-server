package com.samsamhajo.deepground.qna.answer.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AnswerErrorCode implements ErrorCode {

    ANSWER_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "답변 내용을 찾을 수 없습니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "답변을 찾을 수 없습니다."),
    ANSWER_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 찾을 수 없습니다."),
    ANSWER_ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다."),
    ANSWER_MEMBER_MISMTACH(HttpStatus.BAD_REQUEST, "답변을 작성한 사용자가 아닙니다.");


    private final HttpStatus status;
    private final String message;

    AnswerErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}