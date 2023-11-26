package com.coffeekiosk.coffeekiosk.common.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import com.coffeekiosk.coffeekiosk.exception.ErrorCode;

import lombok.Getter;

@Getter
public class ErrorResponse extends CommonResponse {
	private List<FieldError> fieldErrors;

	private ErrorResponse(ErrorCode errorCode, List<FieldError> fieldErrors) {
		super(errorCode.getHttpStatus(), errorCode.getMessage());
		this.fieldErrors = fieldErrors;
	}

	private ErrorResponse(ErrorCode errorCode) {
		super(errorCode.getHttpStatus(), errorCode.getMessage());
		this.fieldErrors = new ArrayList<>();
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode);

	}

	public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
		return new ErrorResponse(errorCode, FieldError.of(bindingResult));
	}

	@Getter
	static class FieldError {
		private String field;
		private String message;

		private FieldError(String field, String message) {
			this.field = field;
			this.message = message;
		}

		private static List<FieldError> of(BindingResult bindingResult) {
			return bindingResult.getFieldErrors()
				.stream()
				.map(statusName -> new FieldError(
					statusName.getField(),
					statusName.getDefaultMessage())
				)
				.collect(Collectors.toList());
		}
	}
}

