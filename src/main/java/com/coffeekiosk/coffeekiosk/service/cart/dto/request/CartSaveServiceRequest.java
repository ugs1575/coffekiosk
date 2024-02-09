package com.coffeekiosk.coffeekiosk.service.cart.dto.request;

import java.util.function.Supplier;

import com.coffeekiosk.coffeekiosk.domain.cart.Cart;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.user.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartSaveServiceRequest {

	private Long userId;

	private Long itemId;

	private int count;

	@Builder
	private CartSaveServiceRequest(Long userId, Long itemId, int count) {
		this.userId = userId;
		this.itemId = itemId;
		this.count = count;
	}
}
