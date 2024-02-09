package com.coffeekiosk.coffeekiosk.service.order.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSaveServiceRequest {

	private List<Long> cartIdList;

	@Builder
	private OrderSaveServiceRequest(List<Long> cartIdList) {
		this.cartIdList = cartIdList;
	}
}
