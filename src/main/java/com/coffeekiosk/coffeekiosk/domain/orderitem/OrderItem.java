package com.coffeekiosk.coffeekiosk.domain.orderitem;

import static jakarta.persistence.FetchType.*;

import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.order.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint( name = "ORDER_ITEM_UNIQUE", columnNames = {"order_id", "item_id"} )
	}
)
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(nullable = false)
	private int orderPrice;

	@Column(nullable = false)
	private int orderCount;

	@Builder
	private OrderItem(Item item, int orderCount) {
		this.item = item;
		this.orderPrice = getTotalPrice(item, orderCount);
		this.orderCount = orderCount;
	}

	public static OrderItem createOrderItem(Item item, int count) {
		return OrderItem.builder()
			.item(item)
			.orderCount(count)
			.build();
	}

	public void mappingOrder(Order order) {
		this.order = order;
	}

	private int getTotalPrice(Item item, int count) {
		return item.getPrice() * count;
	}
}
