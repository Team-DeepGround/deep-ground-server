package com.samsamhajo.deepground.studyGroup.exception;


import com.samsamhajo.deepground.global.error.core.BaseException;
import com.samsamhajo.deepground.global.error.core.ErrorCode;

public class StudyGroupException extends BaseException {
    public StudyGroupException(ErrorCode errorCode) {
        super(errorCode);
    }
}
