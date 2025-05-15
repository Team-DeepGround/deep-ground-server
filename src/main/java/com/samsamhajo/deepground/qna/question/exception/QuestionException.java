package com.samsamhajo.deepground.qna.question.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;
import org.springframework.http.HttpStatus;

public class QuestionException extends BaseException {
    public QuestionException(QuestionErrorCode errorCode) {
        super(errorCode);
    }
}
