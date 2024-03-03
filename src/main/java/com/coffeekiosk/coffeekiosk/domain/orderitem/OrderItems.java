package com.coffeekiosk.coffeekiosk.domain.orderitem;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import com.coffeekiosk.coffeekiosk.domain.order.Order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderItems {

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

	public static OrderItems empty() {
		return new OrderItems();
	}

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
	}

	public int calculateTotalPrice() {
		return orderItems.stream()
			.mapToInt(OrderItem::getOrderPrice)
			.sum();
	}

	public void mappingOrder(Order order) {
		orderItems.forEach(orderItem -> orderItem.mappingOrder(order));
	}
}
