package com.samsamhajo.deepground.qna.answer.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AnswerSuccessCode implements SuccessCode {

    ANSWER_CREATED(HttpStatus.CREATED, "답변이 성공적으로 생성되었습니다.");

    private final HttpStatus status;
    private final String message;

    AnswerSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}