package com.coffeekiosk.coffeekiosk.controller.cart;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.controller.cart.dto.request.CartSaveRequest;
import com.coffeekiosk.coffeekiosk.service.cart.CartService;
import com.coffeekiosk.coffeekiosk.service.cart.dto.response.CartResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/users/{userId}/carts")
@RequiredArgsConstructor
@RestController
public class CartApiController {

	private final CartService cartService;

	@PostMapping
	public ApiResponse<CartResponse> updateCartItem(@PathVariable Long userId, @RequestBody @Valid CartSaveRequest request) {
		CartResponse response = cartService.updateCartItem(userId, request.toServiceRequest());
		return ApiResponse.ok(response);
	}
	@DeleteMapping("/{cartId}")
	public ApiResponse<Void> deleteCartItem(@PathVariable Long userId, @PathVariable Long cartId) {
		cartService.deleteCartItem(cartId, userId);
		return ApiResponse.noContent();
	}

	@GetMapping
	public ApiResponse<List<CartResponse>> findCartItems(@PathVariable Long userId) {
		List<CartResponse> response = cartService.findCartItems(userId);
		return ApiResponse.ok(response);
	}
}
