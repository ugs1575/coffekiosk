package com.coffeekiosk.coffeekiosk.controller.order.api;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.config.auth.LoginUser;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.controller.order.api.dto.request.OrderSaveRequest;
import com.coffeekiosk.coffeekiosk.facade.RedissonLockOrderFacade;
import com.coffeekiosk.coffeekiosk.service.order.OrderHistoryService;
import com.coffeekiosk.coffeekiosk.service.order.dto.response.OrderResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderApiController {

	private final OrderHistoryService orderHistoryService;
	private final RedissonLockOrderFacade orderFacade;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createOrder(
		@LoginUser SessionUser user, @RequestBody @Valid OrderSaveRequest request) throws
		InterruptedException {
		Long orderId = orderFacade.order(user, request.toServiceRequest(), LocalDateTime.now());
		return ResponseEntity.created(URI.create("/api/orders/" + orderId)).body(ApiResponse.created());
	}

	@GetMapping("/{orderId}")
	public ApiResponse<OrderResponse> findOrder(@LoginUser SessionUser user, @PathVariable Long orderId) {
		OrderResponse response = orderHistoryService.findOrder(orderId, user);
		return ApiResponse.ok(response);
	}

	@GetMapping
	public ApiResponse<List<OrderResponse>> findOrders(
		@LoginUser SessionUser user, @PageableDefault(value = 10) Pageable pageable) {
		List<OrderResponse> response = orderHistoryService.findOrders(user, pageable);
		return ApiResponse.ok(response);
	}
}
