package com.coffeekiosk.coffeekiosk.domain.orderitem;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemType;

class OrderItemsTest {

	@DisplayName("빈 OrderItems를 생성한다.")
	@Test
	void empty() {
		//when
		OrderItems actual = OrderItems.empty();

		//then
		assertThat(actual).isInstanceOf(OrderItems.class);
	}

	@DisplayName("OrderItem을 추가한다.")
	@Test
	void addOrderItem() {
		//given
		OrderItems orderItems = new OrderItems();
		OrderItem orderItem = new OrderItem();

		//when
		orderItems.addOrderItem(orderItem);

		//then
		assertThat(orderItems.getOrderItems()).hasSize(1);
	}

	@DisplayName("주문 상품마다 주문 금액을 계산한다.")
	@Test
	void calculateTotalPrice() {
		//given
		Item item1 = createItem("아이스아메리카노", 1000);
		Item item2 = createItem("카페라떼", 2000);

		OrderItem orderItem1 = createOrderItem(item1, 1);
		OrderItem orderItem2 = createOrderItem(item2, 1);
		OrderItems orderItems = new OrderItems(List.of(orderItem1, orderItem2));

		//when
		int totalPrice = orderItems.calculateTotalPrice();

		//then
		assertThat(totalPrice).isEqualTo(3000);
	}

	private Item createItem(String name, int price) {
		return Item.builder()
			.name(name)
			.itemType(ItemType.COFFEE)
			.price(price)
			.lastModifiedDateTime(LocalDateTime.now())
			.build();
	}

	private OrderItem createOrderItem(Item item, int count) {
		return OrderItem.builder()
			.item(item)
			.orderCount(count)
			.build();
	}
}