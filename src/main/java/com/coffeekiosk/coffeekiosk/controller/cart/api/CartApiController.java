package com.coffeekiosk.coffeekiosk.controller.cart.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.config.auth.LoginUser;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.controller.cart.api.dto.request.CartSaveRequest;
import com.coffeekiosk.coffeekiosk.service.cart.CartService;
import com.coffeekiosk.coffeekiosk.service.cart.dto.response.CartResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/carts")
@RequiredArgsConstructor
@RestController
public class CartApiController {

	private final CartService cartService;

	@PostMapping
	public ApiResponse<CartResponse> updateCartItem(
		@LoginUser SessionUser user, @RequestBody @Valid CartSaveRequest request) {
		CartResponse response = cartService.updateCartItem(user, request.toServiceRequest());
		return ApiResponse.ok(response);
	}

	@DeleteMapping("/{cartId}")
	public ApiResponse<Void> deleteCartItem(@LoginUser SessionUser user, @PathVariable Long cartId) {
		cartService.deleteCartItem(cartId, user);
		return ApiResponse.noContent();
	}

	@GetMapping
	public ApiResponse<List<CartResponse>> findCartItems(@LoginUser SessionUser user) {
		List<CartResponse> response = cartService.findCartItems(user);
		return ApiResponse.ok(response);
	}
}
