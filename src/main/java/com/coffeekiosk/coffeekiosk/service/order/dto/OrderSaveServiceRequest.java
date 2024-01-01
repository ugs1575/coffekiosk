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

	private List<OrderItemRequest> orderItems;

	@Builder
	private OrderSaveServiceRequest(List<OrderItemRequest> orderItems) {
		this.orderItems = orderItems;
	}

	public List<Long> getItemIds() {
		return orderItems.stream()
			.map(OrderItemRequest::getItemId)
			.collect(Collectors.toList());
	}

	//todo: 중복되는 Item id 가 있는지 체크
}
