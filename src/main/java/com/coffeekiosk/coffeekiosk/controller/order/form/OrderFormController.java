package com.coffeekiosk.coffeekiosk.controller.order.form;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coffeekiosk.coffeekiosk.controller.order.form.dto.request.OrderSaveForm;
import com.coffeekiosk.coffeekiosk.facade.RedissonLockOrderFacade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class OrderFormController {

	private final RedissonLockOrderFacade orderFacade;

	@PostMapping("/order")
	public String create(@Valid OrderSaveForm orderSaveForm, BindingResult result, RedirectAttributes redirectAttributes) {
		Long userId = 1L;

		orderSaveForm.filterNull();

		// if (result.hasErrors()) {
		// 	return "order/error";
		// }

		Long orderId = orderFacade.order(userId, orderSaveForm.toServiceRequest(), LocalDateTime.now());
		redirectAttributes.addAttribute("orderId", orderId);
		return "redirect:/order/{orderId}";
	}
}
