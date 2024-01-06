package com.coffeekiosk.coffeekiosk.controller.order.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemSaveRequest {

	private Long itemId;

	private int count;

	@Builder
	private OrderItemSaveRequest(Long itemId, int count) {
		this.itemId = itemId;
		this.count = count;
	}


}
