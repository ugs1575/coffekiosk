package com.coffeekiosk.coffeekiosk.controller.order.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import com.coffeekiosk.coffeekiosk.service.order.dto.OrderItemSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.OrderSaveServiceRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSaveRequest {

	@NotEmpty(message = "주문 목록은 필수입니다.")
	private List<@Valid OrderItemSaveRequest> orderList;

	@Builder
	private OrderSaveRequest(List<OrderItemSaveRequest> orderList) {
		this.orderList = orderList;
	}

	public OrderSaveServiceRequest toServiceRequest() {
		List<OrderItemSaveServiceRequest> requests = orderList.stream()
			.map(OrderItemSaveRequest::toServiceRequest)
			.collect(Collectors.toList());

		return OrderSaveServiceRequest.builder()
			.orderList(requests)
			.build();
	}
}
