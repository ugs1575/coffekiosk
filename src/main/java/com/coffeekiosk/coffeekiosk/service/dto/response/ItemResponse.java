package com.coffeekiosk.coffeekiosk.service.dto.response;

import java.time.LocalDateTime;

import com.coffeekiosk.coffeekiosk.domain.Item;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemResponse {

	private Long id;

	private String name;

	private String itemType;

	private int price;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime lastModifiedDateTime;

	@Builder
	private ItemResponse(Long id, String name, String itemType, int price, LocalDateTime lastModifiedDateTime) {
		this.id = id;
		this.name = name;
		this.itemType = itemType;
		this.price = price;
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

	public static ItemResponse of(Item item) {
		return ItemResponse.builder()
			.id(item.getId())
			.name(item.getName())
			.itemType(item.getItemType().getName())
			.price(item.getPrice())
			.lastModifiedDateTime(item.getLastModifiedDateTime())
			.build();
	}
}
