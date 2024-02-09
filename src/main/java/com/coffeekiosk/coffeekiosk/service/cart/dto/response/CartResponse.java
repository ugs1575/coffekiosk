package com.coffeekiosk.coffeekiosk.service.cart.dto.response;

import com.coffeekiosk.coffeekiosk.domain.cart.Cart;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartResponse {

	private Long id;

	private Long itemId;

	private String itemName;

	private int count;

	@Builder
	private CartResponse(Long id, Long itemId, String itemName, int count) {
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.count = count;
	}

	public static CartResponse of(Cart cart) {
		return CartResponse.builder()
			.id(cart.getId())
			.itemId(cart.getItem().getId())
			.itemName(cart.getItem().getName())
			.count(cart.getCount())
			.build();
	}
}
