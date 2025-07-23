package com.samsamhajo.deepground.address.exception;

import com.samsamhajo.deepground.global.error.core.BaseException;
import com.samsamhajo.deepground.global.error.core.ErrorCode;

public class AddressException extends BaseException {
  public AddressException(ErrorCode errorCode) {
    super(errorCode);
  }
}
