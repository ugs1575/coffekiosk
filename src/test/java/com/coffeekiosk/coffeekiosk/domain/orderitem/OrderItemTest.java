package com.coffeekiosk.coffeekiosk.domain.orderitem;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemType;

class OrderItemTest {

	@DisplayName("주문 상품 금액을 계산한다.")
	@Test
	void calculateOrderPrice() {
		//given
		Item item = createItem("아이스아메리카노", 1000);

		//when
		OrderItem orderItem = OrderItem.createOrderItem(item, 3);

		//then
		assertThat(orderItem.getOrderPrice()).isEqualTo(3000);
	}

	private Item createItem(String name, int price) {
		return Item.builder()
			.name(name)
			.itemType(ItemType.COFFEE)
			.price(price)
			.lastModifiedDateTime(LocalDateTime.now())
			.build();
	}

}
