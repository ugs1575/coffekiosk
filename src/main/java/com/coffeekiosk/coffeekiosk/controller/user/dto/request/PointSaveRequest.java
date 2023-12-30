package com.coffeekiosk.coffeekiosk.controller.user.dto.request;

import com.coffeekiosk.coffeekiosk.service.user.dto.request.PointSaveServiceRequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointSaveRequest {

	@Min(value = 10000, message = "충전 최소 금액은 10000원 입니다.")
	@Max(value = 550000, message = "충전 최대 금액은 550000원 입니다.")
	private int amount;

	@Builder
	public PointSaveRequest(int amount) {
		this.amount = amount;
	}

	public PointSaveServiceRequest toServiceRequest() {
		return PointSaveServiceRequest.builder()
			.amount(amount)
			.build();
	}
}
