package com.coffeekiosk.coffeekiosk.controller.cart.form.dto.request;

import com.coffeekiosk.coffeekiosk.service.cart.dto.request.CartSaveServiceRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartSaveForm {

	private Long itemId;

	private int count;

	@Builder
	private CartSaveForm(Long itemId, int count) {
		this.itemId = itemId;
		this.count = count;
	}

	public static CartSaveForm of(Long itemId, int count) {
		return CartSaveForm.builder()
			.itemId(itemId)
			.count(count)
			.build();
	}

	public CartSaveServiceRequest toServiceRequest() {
		return CartSaveServiceRequest.builder()
			.itemId(itemId)
			.count(count)
			.build();
	}
}
