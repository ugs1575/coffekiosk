package com.coffeekiosk.coffeekiosk.domain;

import java.util.Arrays;

import com.coffeekiosk.coffeekiosk.common.exception.InvalidValueException;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum ItemType {
	DESSERT("DESSERT", "디저트"),
	COFFEE("COFFEE", "커피"),
	NON_COFFEE("NON_COFFEE", "기타");

	private final String code;
	private final String name;

	ItemType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static ItemType of(String itemType) {
		return Arrays.stream(ItemType.values())
			.filter(type -> type.code.equals(itemType))
			.findFirst()
			.orElseThrow(() -> new InvalidValueException(ErrorCode.ITEM_TYPE_NOT_FOUND));
	}
}
