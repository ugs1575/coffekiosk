package com.coffeekiosk.coffeekiosk.service.order.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItem;
import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItems;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemResponse {

	private Long itemId;

	private String itemName;

	private int itemPrice;

	private int orderCount;

	private int orderPrice;

	@Builder
	private OrderItemResponse(Long itemId, String itemName, int itemPrice, int orderCount, int orderPrice) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.orderCount = orderCount;
		this.orderPrice = orderPrice;
	}

	public static OrderItemResponse of(OrderItem orderItem) {
		return OrderItemResponse.builder()
			.itemId(orderItem.getItem().getId())
			.itemName(orderItem.getItem().getName())
			.itemPrice(orderItem.getItem().getPrice())
			.orderCount(orderItem.getOrderCount())
			.orderPrice(orderItem.getOrderPrice())
			.build();
	}

	public static List<OrderItemResponse> listOf(OrderItems orderItems) {
		return orderItems.getOrderItems().stream()
			.map(OrderItemResponse::of)
			.collect(Collectors.toList());
	}
}
