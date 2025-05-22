package com.samsamhajo.deepground.calendar.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;
import com.samsamhajo.deepground.global.error.core.ErrorCode;

public class ScheduleException extends BaseException {
    public ScheduleException(ErrorCode errorCode) { super(errorCode); }
}
