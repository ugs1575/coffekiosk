package com.coffeekiosk.coffeekiosk.controller.cart.form;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.coffeekiosk.coffeekiosk.config.auth.LoginUser;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
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

	@GetMapping("/cart/list")
	public String update(Model model, @LoginUser SessionUser sessionUser) {
		List<CartResponse> carts = cartService.findCartItems(sessionUser);
		model.addAttribute("cartItems", carts);

		UserResponse user = userService.findUser(sessionUser);
		model.addAttribute("user", user);

		OrderSaveForm orderSaveForm = new OrderSaveForm();

		for (CartResponse cart : carts) {
			orderSaveForm.addCartId(cart.getId());
		}

		model.addAttribute("orderSaveForm", orderSaveForm);
		return "cart/listForm";
	}
}
