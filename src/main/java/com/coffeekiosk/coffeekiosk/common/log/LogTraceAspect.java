package com.coffeekiosk.coffeekiosk.common.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class LogTraceAspect {

	private final LogTrace logTrace;

	public LogTraceAspect(LogTrace logTrace) {
		this.logTrace = logTrace;
	}

	@Around("execution(* com.coffeekiosk.coffeekiosk.controller..*(..))")
	public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
		TraceStatus status = null;
		try {
			String message = joinPoint.getSignature().toShortString();
			status = logTrace.begin(message);

			Object proceed = joinPoint.proceed();

			logTrace.end(status);
			return proceed;
		} catch (Exception e) {
			logTrace.exception(status, e);
			throw e;
		}
	}
}
