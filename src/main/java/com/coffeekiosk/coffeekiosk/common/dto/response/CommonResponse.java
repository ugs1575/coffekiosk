package com.coffeekiosk.coffeekiosk.common.dto.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponse {
	private int code;
	private String message;

	public CommonResponse(HttpStatus status, String message) {
		this.code = status.value();
		this.message = message;
	}
}
