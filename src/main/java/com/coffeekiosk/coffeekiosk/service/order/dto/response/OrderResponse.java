package com.coffeekiosk.coffeekiosk.service.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.coffeekiosk.coffeekiosk.domain.order.Order;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponse {

	private Long id;

	private int totalPrice;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime orderDateTime;

	private List<OrderItemResponse> orderItems;

	@Builder
	private OrderResponse(Long id, int totalPrice, List<OrderItemResponse> orderItems, LocalDateTime orderDateTime) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.orderItems = orderItems;
		this.orderDateTime = orderDateTime;
	}

	public static OrderResponse of(Order order) {
		return OrderResponse.builder()
			.id(order.getId())
			.totalPrice(order.getOrderPrice())
			.orderItems(OrderItemResponse.listOf(order.getOrderItems()))
			.orderDateTime(order.getOrderDateTime())
			.build();
	}

	public static List<OrderResponse> listOf(List<Order> orders) {
		return orders.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}

	public static List<OrderResponse> listOf(Page<Order> orders) {
		return orders.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}
}
