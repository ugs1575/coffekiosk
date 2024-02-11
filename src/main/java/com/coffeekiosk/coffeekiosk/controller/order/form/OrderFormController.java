package com.coffeekiosk.coffeekiosk.controller.order.form;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.controller.order.form.dto.request.OrderSaveForm;
import com.coffeekiosk.coffeekiosk.domain.order.Order;
import com.coffeekiosk.coffeekiosk.facade.RedissonLockOrderFacade;
import com.coffeekiosk.coffeekiosk.service.order.OrderHistoryService;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSearchServiceRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.response.OrderResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/order")
@RequiredArgsConstructor
@Controller
public class OrderFormController {

	private final OrderHistoryService orderHistoryService;
	private final RedissonLockOrderFacade orderFacade;

	@PostMapping
	public String create(@Valid OrderSaveForm orderSaveForm, BindingResult result, RedirectAttributes redirectAttributes) {
		Long userId = 1L;

		orderSaveForm.filterNull();

		if (result.hasErrors()) {
			return "order/error";
		}

		Long orderId = orderFacade.order(userId, orderSaveForm.toServiceRequest(), LocalDateTime.now());
		redirectAttributes.addAttribute("orderId", orderId);
		return "redirect:/order/{orderId}";
	}

	@GetMapping("/{orderId}")
	public String findOrder(Model model, @PathVariable Long orderId) {
		Long userId = 1L;

		try {
			OrderResponse order = orderHistoryService.findOrder(orderId, userId);
			model.addAttribute("order", order);
			return "order/detailForm";
		} catch (BusinessException e) {
			return "order/error";
		}
	}

	@GetMapping("/history")
	public String findOrders(Model model, @PageableDefault(size = 10) Pageable pageable) {
		Long userId = 1L;

		LocalDateTime endDate = LocalDateTime.now();
		LocalDateTime startDate = LocalDateTime.now().minusYears(3);

		OrderSearchServiceRequest request = OrderSearchServiceRequest.builder()
			.startDate(startDate)
			.endDate(endDate)
			.build();

		Page<OrderResponse> pageOrders = orderHistoryService.findPageOrders(userId, request, pageable);
		model.addAttribute("orders", pageOrders);
		return "order/history";
	}
}
