package com.coffeekiosk.coffeekiosk.service.order.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemRequest {

	private Long itemId;

	private int count;

	@Builder
	private OrderItemRequest(Long itemId, int count) {
		this.itemId = itemId;
		this.count = count;
	}


}
