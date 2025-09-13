package com.samsamhajo.deepground.studyGroup.exception;

import com.samsamhajo.deepground.global.error.ErrorCode;
import com.samsamhajo.deepground.global.error.exception.BusinessException;

public class StudyGroupException extends BusinessException {
    public StudyGroupException(ErrorCode errorCode) {
        super(errorCode);
    }
}
