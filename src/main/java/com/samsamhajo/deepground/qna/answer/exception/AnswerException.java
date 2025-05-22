package com.samsamhajo.deepground.qna.answer.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;

public class AnswerException extends BaseException {
    public AnswerException(AnswerErrorCode errorCode) {
        super(errorCode);
    }
}