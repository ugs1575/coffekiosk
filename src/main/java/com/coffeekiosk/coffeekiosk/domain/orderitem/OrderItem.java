package com.coffeekiosk.coffeekiosk.domain.orderitem;

import static jakarta.persistence.FetchType.*;

import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.order.Order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	private int orderPrice;

	private int count;

	@Builder
	private OrderItem(Item item, int count) {
		this.item = item;
		this.orderPrice = getTotalPrice(item, count);
		this.count = count;
	}

	public static OrderItem createOrderItem(Item item, int count) {
		return OrderItem.builder()
			.item(item)
			.count(count)
			.build();
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	private int getTotalPrice(Item item, int count) {
		return item.getPrice() * count;
	}
}
