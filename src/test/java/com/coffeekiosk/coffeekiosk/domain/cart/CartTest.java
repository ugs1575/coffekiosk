package com.coffeekiosk.coffeekiosk.domain.cart;

import static com.coffeekiosk.coffeekiosk.domain.item.ItemType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.user.User;

class CartTest {

	@DisplayName("장바구니에 담긴 수량이 20개 이상인지 확인한다.")
	@Test
	void addCount() {
		//given
		Item item = createItem();
		User user = createUser();

		Cart cart = Cart.createCart(user, item, 20);

		//when, then
		Assertions.assertThatThrownBy(() -> cart.addCount(1))
			.isInstanceOf(BusinessException.class)
			.hasMessage("최대 주문 가능 수량은 20개 입니다.");
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