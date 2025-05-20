package com.samsamhajo.deepground.qna.question.exception;

import com.samsamhajo.deepground.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum QuestionSuccessCode implements SuccessCode {

    QUESTION_CREATED(HttpStatus.CREATED, "질문이 성공적으로 생성되었습니다."),
    QUESTION_DELETED(HttpStatus.OK, "질문이 성공적으로 삭제되었습니다.");

    private final HttpStatus status;
    private final String message;

    QuestionSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
