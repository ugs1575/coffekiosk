package com.coffeekiosk.coffeekiosk.controller.order;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.common.dto.response.CreatedResponse;
import com.coffeekiosk.coffeekiosk.controller.order.dto.request.OrderSaveRequest;
import com.coffeekiosk.coffeekiosk.service.order.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/users/{userId}/orders")
@RequiredArgsConstructor
@RestController
public class OrderApiController {

	private final OrderService orderService;

	@PostMapping
	public ApiResponse<CreatedResponse> createOrder(@PathVariable Long userId, @RequestBody @Valid OrderSaveRequest request) {
		Long orderId = orderService.order(userId, request.toServiceRequest(), LocalDateTime.now());
		return ApiResponse.created(orderId);
	}
}
