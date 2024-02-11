package com.coffeekiosk.coffeekiosk.controller.cart.form;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.controller.cart.form.dto.request.CartSaveForm;
import com.coffeekiosk.coffeekiosk.controller.order.form.dto.request.OrderSaveForm;
import com.coffeekiosk.coffeekiosk.service.cart.CartService;
import com.coffeekiosk.coffeekiosk.service.cart.dto.response.CartResponse;
import com.coffeekiosk.coffeekiosk.service.user.UserService;
import com.coffeekiosk.coffeekiosk.service.user.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CartFormController {

	private final CartService cartService;
	private final UserService userService;

	@ResponseBody
	@PostMapping("/cart/add")
	public ApiResponse<CartResponse> create(
		@RequestParam Long itemId,
		@RequestParam(defaultValue = "1") int count
	) {
		Long userId = 1L;

		try {
			CartResponse response = cartService.updateCartItem(userId, CartSaveForm.of(itemId, count).toServiceRequest());
			return ApiResponse.ok(response);
		} catch (BusinessException e) {
			return ApiResponse.of(HttpStatus.OK, e.getErrorCode().getCode(), null);
		}
	}

	@GetMapping("/cart/list")
	public String update(Model model) {
		Long userId = 1L;

		List<CartResponse> carts = cartService.findCartItems(userId);
		model.addAttribute("cartItems", carts);

		UserResponse user = userService.findUser(userId);
		model.addAttribute("user", user);

		OrderSaveForm orderSaveForm = new OrderSaveForm();

		for (CartResponse cart : carts) {
			orderSaveForm.addCartId(cart.getId());
		}

		model.addAttribute("orderSaveForm", orderSaveForm);
		return "cart/listForm";
	}

	@ResponseBody
	@PostMapping("/cart/delete")
	public ApiResponse<Void> create(@RequestParam Long cartId) {
		Long userId = 1L;
		cartService.deleteCartItem(cartId, userId);
		return ApiResponse.noContent();
	}
}
