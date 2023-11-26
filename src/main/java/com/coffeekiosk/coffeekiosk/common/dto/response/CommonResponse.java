package com.coffeekiosk.coffeekiosk.common.dto.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CommonResponse {
	private int code;
	private String message;

	public CommonResponse(HttpStatus status, String message) {
		this.code = status.value();
		this.message = message;
	}
}
