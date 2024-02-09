package com.coffeekiosk.coffeekiosk.service.cart.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartSaveServiceRequest {

	private Long itemId;

	private int count;

	@Builder
	private CartSaveServiceRequest(Long itemId, int count) {
		this.itemId = itemId;
		this.count = count;
	}
}
