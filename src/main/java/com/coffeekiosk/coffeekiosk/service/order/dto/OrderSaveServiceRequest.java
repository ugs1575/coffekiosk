package com.coffeekiosk.coffeekiosk.service.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSaveServiceRequest {

	private List<OrderItemSaveServiceRequest> orderItems;

	@Builder
	private OrderSaveServiceRequest(List<OrderItemSaveServiceRequest> orderItems) {
		this.orderItems = orderItems;
	}

	public List<Long> getItemIds() {
		return orderItems.stream()
			.map(OrderItemSaveServiceRequest::getItemId)
			.collect(Collectors.toList());
	}
}
