package com.coffeekiosk.coffeekiosk.controller.item.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemUpdateServiceRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemUpdateRequest {

	@NotBlank(message = "상품 이름은 필수입니다.")
	private String name;

	@NotBlank(message = "상품 타입은 필수입니다.")
	private String itemType;

	@Min(value = 1, message = "최소 상품 가격은 1원입니다.")
	private int price;

	@Builder
	private ItemUpdateRequest(String name, String itemType, int price) {
		this.name = name;
		this.itemType = itemType;
		this.price = price;
	}

	public ItemUpdateServiceRequest toServiceRequest() {
		return ItemUpdateServiceRequest.builder()
			.name(name)
			.itemType(ItemType.of(itemType))
			.price(price)
			.build();

	}
}
