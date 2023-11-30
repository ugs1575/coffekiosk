package com.coffeekiosk.coffeekiosk.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItemTest {

	@DisplayName("상품을 등록 시 마지막 수정시간을 기록한다.")
	@Test
	void create() {

		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		Item item = Item.builder()
			.name("카페라떼")
			.itemType(ItemType.COFFEE)
			.price(5000)
			.build();

		//when
		Item savedItem = item.create(lastModifiedDateTime);

		//then
		assertThat(savedItem.getLastModifiedDateTime()).isEqualTo(lastModifiedDateTime);
	}

	@DisplayName("상품 정보를 수정한다.")
	@Test
	void update() {

		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		LocalDateTime updatedModifiedDateTime = LocalDateTime.of(2023, 11, 22, 0, 0);

		Item item = Item.builder()
			.name("카페라떼")
			.itemType(ItemType.COFFEE)
			.price(5000)
			.lastModifiedDateTime(lastModifiedDateTime)
			.build();

		Item updateRequest = Item.builder()
			.name("케이크")
			.itemType(ItemType.DESSERT)
			.price(6000)
			.build();

		//when
		Item updatedItem = item.update(updateRequest, updatedModifiedDateTime);

		//then
		assertThat(updatedItem.getName()).isEqualTo("케이크");
		assertThat(updatedItem.getItemType()).isEqualTo(ItemType.DESSERT);
		assertThat(updatedItem.getPrice()).isEqualTo(6000);
		assertThat(updatedItem.getLastModifiedDateTime()).isEqualTo(updatedModifiedDateTime);
	}
}