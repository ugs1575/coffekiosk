package com.coffeekiosk.coffeekiosk.service.order.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSaveServiceRequest {

	private List<OrderItemSaveServiceRequest> orderList;

	@Builder
	private OrderSaveServiceRequest(List<OrderItemSaveServiceRequest> orderList) {
		this.orderList = orderList;
	}

	public List<Long> getItemIds() {
		return orderList.stream()
			.map(OrderItemSaveServiceRequest::getItemId)
			.collect(Collectors.toList());
	}
}
