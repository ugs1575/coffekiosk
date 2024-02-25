package com.coffeekiosk.coffeekiosk.controller.user.form.dto.request;

import com.coffeekiosk.coffeekiosk.service.user.dto.request.PointSaveServiceRequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PointSaveForm {

	@NotNull(message = "충전 최소 금액은 필수입니다.")
	@Min(value = 10000, message = "충전 최소 금액은 10000원 입니다.")
	@Max(value = 550000, message = "충전 최대 금액은 550000원 입니다.")
	private Integer amount;

	@Builder
	public PointSaveForm(int amount) {
		this.amount = amount;
	}

	public PointSaveServiceRequest toServiceRequest() {
		return PointSaveServiceRequest.builder()
			.amount(amount)
			.build();
	}
}
