package com.coffeekiosk.coffeekiosk.service.dto.request;

import com.coffeekiosk.coffeekiosk.domain.ItemType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemSearchServiceRequest {

	private String name;

	private ItemType itemType;

	@Builder
	private ItemSearchServiceRequest(String name, ItemType itemType) {
		this.name = name;
		this.itemType = itemType;
	}
}
