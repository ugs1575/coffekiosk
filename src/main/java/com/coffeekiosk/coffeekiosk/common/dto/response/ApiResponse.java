package com.coffeekiosk.coffeekiosk.common.dto.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> extends CommonResponse {
	private T data;

	private ApiResponse(HttpStatus status, String message, T data) {
		super(status, message);
		this.data = data;
	}

	public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
		return new ApiResponse<>(httpStatus, message, data);
	}

	public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
		return of(httpStatus, httpStatus.name(), data);
	}

	public static <T> ApiResponse<T> ok(T data) {
		return of(HttpStatus.OK, HttpStatus.OK.name(), data);
	}

	public static <T> ApiResponse<T> noContent() {
		return of(HttpStatus.OK, HttpStatus.OK.name(), null);
	}
}

