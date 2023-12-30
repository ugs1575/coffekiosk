package com.coffeekiosk.coffeekiosk.service.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointSaveServiceRequest {

	private int amount;

	@Builder
	private PointSaveServiceRequest(int amount) {
		this.amount = amount;
	}
}
