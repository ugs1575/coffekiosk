package com.coffeekiosk.coffeekiosk.common.exception;

import com.coffeekiosk.coffeekiosk.exception.ErrorCode;

public class InvalidValueException extends BusinessException {
	public InvalidValueException(ErrorCode errorCode) {
		super(errorCode);
	}
}
