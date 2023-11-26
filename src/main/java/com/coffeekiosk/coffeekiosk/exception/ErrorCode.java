package com.coffeekiosk.coffeekiosk.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),

	ITEM_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품 타입을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
