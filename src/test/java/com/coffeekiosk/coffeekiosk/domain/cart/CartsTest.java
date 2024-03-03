package com.coffeekiosk.coffeekiosk.domain.cart;

import static com.coffeekiosk.coffeekiosk.domain.item.ItemType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.user.User;

class CartsTest {
	@DisplayName("장바구니 수량이 20개가 넘는지 확인한다.")
	@Test
	void isOverMaxOrderCount() {
		//given
		Item item1 = createItem();
		Item item2 = createItem();
		User user = createUser();

		Cart cart1 = createCart(user, item1, 10);
		Cart cart2 = createCart(user, item2, 10);

		Carts carts = new Carts(List.of(cart1, cart2));

		//when
		boolean result = carts.isOverMaxOrderCount(1);

		//then
		assertThat(result).isTrue();
	}

	private Cart createCart(User user, Item item, int count) {
		return Cart.builder()
			.user(user)
			.item(item)
			.itemCount(count)
			.build();
	}

	private Item createItem() {
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		return Item.builder()
			.name("카페라떼")
			.itemType(COFFEE)
			.price(5000)
			.lastModifiedDateTime(lastModifiedDateTime)
			.build();
	}

	private User createUser() {
		return User.builder()
			.name("우경서")
			.build();
	}
}
