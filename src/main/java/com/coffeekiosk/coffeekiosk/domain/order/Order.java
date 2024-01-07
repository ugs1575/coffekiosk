package com.coffeekiosk.coffeekiosk.domain.order;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.List;

import com.coffeekiosk.coffeekiosk.common.domain.BaseTimeEntity;
import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItem;
import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItems;
import com.coffeekiosk.coffeekiosk.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@Embedded
	private OrderItems orderItems = OrderItems.empty();

	private LocalDateTime orderDateTime;

	@Builder
	private Order(User user, LocalDateTime orderDateTime) {
		this.user = user;
		this.orderDateTime = orderDateTime;
	}

	public static Order order(User user, List<OrderItem> orderItems, LocalDateTime orderDateTime) {
		Order order = Order.builder()
			.user(user)
			.orderDateTime(orderDateTime)
			.build();

		for (OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}

		return order;
	}

	public void addOrderItem(OrderItem orderItem) {
		orderItems.addOrderItem(orderItem);
		orderItem.setOrder(this);
	}

	public int calculateTotalPrice() {
		return orderItems.calculateTotalPrice();
	}
}
