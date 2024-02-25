package com.coffeekiosk.coffeekiosk.common.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse extends CommonResponse {
	private List<FieldError> fieldErrors;

	private ErrorResponse(ErrorCode errorCode, List<FieldError> fieldErrors) {
		super(errorCode.getCode(), errorCode.getMessage());
		this.fieldErrors = fieldErrors;
	}

	private ErrorResponse(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
		this.fieldErrors = new ArrayList<>();
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode);

	}

	public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
		return new ErrorResponse(errorCode, FieldError.of(bindingResult));
	}

	public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
		List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getName(), ErrorCode.INVALID_TYPE_VALUE.getMessage());
		return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
	}

	public static ErrorResponse of(HttpMessageNotReadableException e) {
		if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
			String fieldName = mismatchedInputException.getPath().get(0).getFieldName();
			List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(fieldName, ErrorCode.INVALID_JSON_FORMAT.getMessage());
			return new ErrorResponse(ErrorCode.INVALID_JSON_FORMAT, errors);
		}

		return new ErrorResponse(ErrorCode.INVALID_JSON_FORMAT);
	}

	public static ErrorResponse of(MissingServletRequestParameterException e) {
		List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getParameterName(), ErrorCode.MISSING_REQUEST_PARAM.getMessage());
		return new ErrorResponse(ErrorCode.MISSING_REQUEST_PARAM, errors);
	}

	public static ErrorResponse of(ErrorCode errorCode, ConstraintViolationException e) {
		return new ErrorResponse(errorCode, FieldError.of(e));
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	static class FieldError {
		private String field;
		private String message;

		private FieldError(String field, String message) {
			this.field = field;
			this.message = message;
		}

		private static List<FieldError> of(ConstraintViolationException e) {
			return e.getConstraintViolations()
				.stream()
				.map(error -> new FieldError(
						error.getPropertyPath().toString(),
						error.getMessage()
					)
				)
				.collect(Collectors.toList());
		}

		private static List<FieldError> of(BindingResult bindingResult) {
			return bindingResult.getFieldErrors()
				.stream()
				.map(error -> new FieldError(
						error.getField(),
						error.getDefaultMessage()
					)
				)
				.collect(Collectors.toList());
		}

		public static List<FieldError> of(String field, String message) {
			List<FieldError> fieldErrors = new ArrayList<>();
			fieldErrors.add(new FieldError(field, message));
			return fieldErrors;
		}
	}
}

