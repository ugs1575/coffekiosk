package com.coffeekiosk.coffeekiosk.service.order.dto.request;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSearchServiceRequest {

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	@Builder
	private OrderSearchServiceRequest(LocalDateTime startDate, LocalDateTime endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
}
