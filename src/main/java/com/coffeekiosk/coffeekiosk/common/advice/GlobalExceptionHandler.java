package com.coffeekiosk.coffeekiosk.common.advice;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.coffeekiosk.coffeekiosk.common.dto.response.ErrorResponse;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
		MissingServletRequestParameterException exception) {
		ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
		ErrorResponse response = ErrorResponse.of(exception);
		return new ResponseEntity<>(response, errorCode.getHttpStatus());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
		HttpMessageNotReadableException exception) {
		ErrorResponse response = ErrorResponse.of(exception);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException exception) {
		ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
		ErrorResponse response = ErrorResponse.of(errorCode, exception.getBindingResult());
		return new ResponseEntity<>(response, errorCode.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException exception) {
		ErrorResponse response = ErrorResponse.of(exception);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException exception) {
		ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		ErrorResponse response = ErrorResponse.of(errorCode);
		return new ResponseEntity<>(response, errorCode.getHttpStatus());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
		ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
		ErrorResponse response = ErrorResponse.of(errorCode, exception);
		return new ResponseEntity<>(response, errorCode.getHttpStatus());
	}

	@ExceptionHandler(IllegalStateException.class)
	protected ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException exception) {
		log.error(exception.getMessage(), exception);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
		log.error(exception.getMessage(), exception);
		ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
