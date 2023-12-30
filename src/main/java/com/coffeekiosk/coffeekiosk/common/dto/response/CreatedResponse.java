package com.coffeekiosk.coffeekiosk.common.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatedResponse {

	private Long id;

	private CreatedResponse(Long id) {
		this.id = id;
	}

	public static CreatedResponse of(Long id) {
		return new CreatedResponse(id);
	}
}