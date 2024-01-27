package com.coffeekiosk.coffeekiosk.controller.order;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.controller.order.dto.request.OrderSaveRequest;
import com.coffeekiosk.coffeekiosk.facade.RedissonLockOrderFacade;
import com.coffeekiosk.coffeekiosk.service.order.OrderHistoryService;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSearchServiceRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.response.OrderResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/users/{userId}/orders")
@RequiredArgsConstructor
@RestController
public class OrderApiController {

	private final OrderHistoryService orderHistoryService;
	private final RedissonLockOrderFacade orderFacade;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createOrder(@PathVariable Long userId, @RequestBody @Valid OrderSaveRequest request) throws
		InterruptedException {
		Long orderId = orderFacade.order(userId, request.toServiceRequest(), LocalDateTime.now());
		return ResponseEntity.created(URI.create("/api/users/"+ userId +"/orders/" + orderId)).body(ApiResponse.created());
	}

	@GetMapping("/{orderId}")
	public ApiResponse<OrderResponse> findOrder(@PathVariable Long userId, @PathVariable Long orderId) {
		OrderResponse response = orderHistoryService.findOrder(orderId, userId);
		return ApiResponse.ok(response);
	}

	@GetMapping
	public ApiResponse<List<OrderResponse>> findOrders(
		@PathVariable Long userId,
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("startDate") LocalDateTime startDate,
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam("endDate") LocalDateTime endDate,
		@PageableDefault(value = 10) Pageable pageable
	) {
		OrderSearchServiceRequest request = OrderSearchServiceRequest.builder()
			.startDate(startDate)
			.endDate(endDate)
			.build();

		List<OrderResponse> response = orderHistoryService.findOrders(userId, request, pageable);
		return ApiResponse.ok(response);
	}
}
