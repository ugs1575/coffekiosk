package com.coffeekiosk.coffeekiosk.controller.order.api.dto.request;

import java.util.List;

import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSaveServiceRequest;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSaveRequest {

	@NotEmpty(message = "주문 목록은 필수입니다.")
	private List<Long> cartIdList;

	@Builder
	private OrderSaveRequest(List<Long> cartIdList) {
		this.cartIdList = cartIdList;
	}

	public OrderSaveServiceRequest toServiceRequest() {
		return OrderSaveServiceRequest.builder()
			.cartIdList(cartIdList)
			.build();
	}
}
