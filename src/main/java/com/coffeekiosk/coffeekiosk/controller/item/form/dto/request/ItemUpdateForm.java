package com.coffeekiosk.coffeekiosk.controller.item.form.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemUpdateServiceRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemUpdateForm {
	@NotNull(message = "상품 ID는 필수입니다.")
	@Positive(message = "상품 ID는 양수입니다.")
	private Long id;

	@NotBlank(message = "상품 이름은 필수입니다.")
	private String name;

	@NotBlank(message = "상품 타입은 필수입니다.")
	private String itemType;

	@NotNull(message = "상품 가격은 필수입니다.")
	@Min(value = 1, message = "최소 상품 가격은 1원입니다.")
	private Integer price;

	@Builder
	private ItemUpdateForm(String name, String itemType, int price) {
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
