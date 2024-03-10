package com.coffeekiosk.coffeekiosk.common.log;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogTrace {

	private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

	public TraceStatus begin(String message) {
		syncTraceId();

		long startTimeMs = System.currentTimeMillis();
		TraceId traceId = traceIdHolder.get();
		log.info("[{}][start] {}", traceId.getId(), message);

		return new TraceStatus(traceId, startTimeMs, message);
	}

	private void syncTraceId() {
		TraceId traceId = traceIdHolder.get();

		if (traceId == null) {
			traceIdHolder.set(new TraceId());
		}
	}

	public void end(TraceStatus status) {
		complete(status, null);
	}

	public void exception(TraceStatus status, Exception e) {
		complete(status, e);
	}

	private void complete(TraceStatus status, Exception e) {
		long stopTimeMs = System.currentTimeMillis();
		long resultTimeMs = stopTimeMs - status.getStartTimeMs();
		TraceId traceId = status.getTraceId();
		if (e == null) {
			log.info("[{}][end] {} time={}ms", traceId.getId(), status.getMessage(), resultTimeMs);
		} else {
			log.info("[{}][ex] {} time={}ms ex={}", traceId.getId(), status.getMessage(), resultTimeMs, e.toString());
		}

		releaseTraceId();
	}

	private void releaseTraceId() {
		traceIdHolder.remove();
	}
}
