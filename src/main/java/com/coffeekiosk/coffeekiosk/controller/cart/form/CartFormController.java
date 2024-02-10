package com.coffeekiosk.coffeekiosk.controller.cart.form;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.controller.cart.form.dto.request.CartSaveForm;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.cart.CartService;
import com.coffeekiosk.coffeekiosk.service.cart.dto.response.CartResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/users/{userId}/carts")
@RequiredArgsConstructor
@RestController
public class CartFormController {

	private final CartService cartService;

	@PostMapping
	public ApiResponse<CartResponse> create(
		@PathVariable Long userId,
		@RequestParam Long itemId,
		@RequestParam(defaultValue = "1") int count
	) {
		try {
			CartResponse response = cartService.updateCartItem(userId, CartSaveForm.of(itemId, count).toServiceRequest());
			return ApiResponse.ok(response);
		} catch (BusinessException e) {
			return ApiResponse.of(HttpStatus.OK, e.getErrorCode().getCode(), null);
		}
	}
}
