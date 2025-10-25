package com.samsamhajo.deepground.support.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;
import com.samsamhajo.deepground.global.error.core.ErrorCode;

public class InquiryException extends BaseException {

    public InquiryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
