package com.coffeekiosk.coffeekiosk.service.dto.request;

import java.time.LocalDateTime;

import com.coffeekiosk.coffeekiosk.domain.Item;
import com.coffeekiosk.coffeekiosk.domain.ItemType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemSaveServiceRequest {

	private String name;

	private ItemType itemType;

	private int price;

	@Builder
	private ItemSaveServiceRequest(String name, ItemType itemType, int price) {
		this.name = name;
		this.itemType = itemType;
		this.price = price;
	}

	public Item toEntity() {
		return Item.builder()
			.name(name)
			.itemType(itemType)
			.price(price)
			.build();
	}
}
