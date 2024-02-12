package com.coffeekiosk.coffeekiosk.controller.item.form.dto.request;

import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSaveServiceRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemSaveForm {

	@NotBlank(message = "상품 이름은 필수입니다.")
	private String name;

	@NotBlank(message = "상품 타입은 필수입니다.")
	private String itemType;

	@NotNull(message = "상품 가격은 필수입니다.")
	@Min(value = 1, message = "최소 상품 가격은 1원입니다.")
	private Integer price;

	@Builder
	private ItemSaveForm(String name, String itemType, int price) {
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
