package com.samsamhajo.deepground.global.upload.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;

public class UploadException extends BaseException {

    public UploadException(UploadErrorCode errorCode) {super(errorCode);}
}
