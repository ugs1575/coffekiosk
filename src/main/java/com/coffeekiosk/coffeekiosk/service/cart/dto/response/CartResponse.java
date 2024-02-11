package com.coffeekiosk.coffeekiosk.service.cart.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.coffeekiosk.coffeekiosk.domain.cart.Cart;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartResponse {

	private Long id;

	private Long itemId;

	private String itemName;

	private int itemPrice;

	private int itemCount;

	@Builder
	private CartResponse(Long id, Long itemId, String itemName, int itemPrice, int itemCount) {
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.itemCount = itemCount;
	}

	public static CartResponse of(Cart cart) {
		return CartResponse.builder()
			.id(cart.getId())
			.itemId(cart.getItem().getId())
			.itemName(cart.getItem().getName())
			.itemPrice(cart.getItem().getPrice())
			.itemCount(cart.getItemCount())
			.build();
	}

	public static List<CartResponse> listOf(List<Cart> cartItems) {
		return cartItems.stream()
			.map(CartResponse::of)
			.collect(Collectors.toList());
	}
}
