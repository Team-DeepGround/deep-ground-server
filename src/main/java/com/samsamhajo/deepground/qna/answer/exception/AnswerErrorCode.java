package com.samsamhajo.deepground.qna.answer.exception;

import com.samsamhajo.deepground.global.error.core.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AnswerErrorCode implements ErrorCode {

    ANSWER_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "답변 내용을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String message;

    AnswerErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}