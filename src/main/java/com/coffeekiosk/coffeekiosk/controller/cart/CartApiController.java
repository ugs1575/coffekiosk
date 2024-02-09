package com.coffeekiosk.coffeekiosk.controller.cart;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.controller.order.dto.request.OrderItemSaveRequest;
import com.coffeekiosk.coffeekiosk.service.cart.CartService;
import com.coffeekiosk.coffeekiosk.service.cart.dto.response.CartResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/users/{userId}/cart")
@RequiredArgsConstructor
@RestController
public class CartApiController {

	private final CartService cartService;

	@PostMapping
	public ApiResponse<CartResponse> updateCart(@PathVariable Long userId, @RequestBody @Valid OrderItemSaveRequest request) {
		CartResponse response = cartService.updateCart(userId, request.toServiceRequest());
		return ApiResponse.ok(response);
	}
}
