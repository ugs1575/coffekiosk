package com.coffeekiosk.coffeekiosk.controller.dto.request;

import static org.springframework.util.StringUtils.*;

import com.coffeekiosk.coffeekiosk.domain.ItemType;
import com.coffeekiosk.coffeekiosk.service.dto.request.ItemSearchServiceRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
