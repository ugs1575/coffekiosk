package com.coffeekiosk.coffeekiosk.service.order.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemSaveServiceRequest {

	private Long itemId;

	private int count;

	@Builder
	private OrderItemSaveServiceRequest(Long itemId, int count) {
		this.itemId = itemId;
		this.count = count;
	}
}
