package com.coffeekiosk.coffeekiosk.controller.item.api.dto.request;

import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSaveServiceRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemSaveRequest {

	@NotBlank(message = "상품 이름은 필수입니다.")
	private String name;

	@NotBlank(message = "상품 타입은 필수입니다.")
	private String itemType;

	@Positive(message = "최소 상품 가격은 1원입니다.")
	private int price;

	@Builder
	private ItemSaveRequest(String name, String itemType, int price) {
		this.name = name;
		this.itemType = itemType;
		this.price = price;
	}

	public ItemSaveServiceRequest toServiceRequest() {
		return ItemSaveServiceRequest.builder()
			.name(name)
			.itemType(ItemType.of(itemType))
			.price(price)
			.build();

	}
}
