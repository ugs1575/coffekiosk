package com.coffeekiosk.coffeekiosk.service.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.order.Order;
import com.coffeekiosk.coffeekiosk.domain.order.OrderRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSearchServiceRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.response.OrderResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderHistoryService {
	private static final int MAX_HISTORY_DATE = 3;

	private final OrderRepository orderRepository;

	public OrderResponse findOrder(Long orderId, Long userId) {
		Order order = orderRepository.findByIdFetchJoin(orderId, userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		return OrderResponse.of(order);
	}

	public List<OrderResponse> findOrders(Long userId, Pageable pageable) {
		OrderSearchServiceRequest request = createSearchRequest();

		List<Order> orders = orderRepository.findOrders(userId, request, pageable);
		return OrderResponse.listOf(orders);
	}

	public Page<OrderResponse> findPageOrders(Long userId, Pageable pageable) {
		OrderSearchServiceRequest request = createSearchRequest();
		Page<Order> orders = orderRepository.findPageOrders(userId, request, pageable);
		Page<OrderResponse> pageOrders = new PageImpl(OrderResponse.listOf(orders), pageable, orders.getTotalElements());
		return pageOrders;
	}

	private OrderSearchServiceRequest createSearchRequest() {
		LocalDateTime endDate = LocalDateTime.now();
		LocalDateTime startDate = LocalDateTime.now().minusYears(MAX_HISTORY_DATE);

		return OrderSearchServiceRequest.builder()
			.startDate(startDate)
			.endDate(endDate)
			.build();
	}
}
