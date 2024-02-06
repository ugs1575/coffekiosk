package com.coffeekiosk.coffeekiosk.controller.item.api.dto.request;

import static org.springframework.util.StringUtils.*;

import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSearchServiceRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemSearchRequest {

	private String name;

	private String itemType;

	@Builder
	private ItemSearchRequest(String name, String itemType) {
		this.name = name;
		this.itemType = itemType;
	}

	public ItemSearchServiceRequest toServiceRequest() {
		return ItemSearchServiceRequest.builder()
			.name(name)
			.itemType(checkType(itemType))
			.build();
	}

	private ItemType checkType(String itemType) {
		return hasText(itemType) ? ItemType.of(itemType) : null;
	}
}
