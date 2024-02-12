package com.coffeekiosk.coffeekiosk.controller.order.api.dto.request;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSaveServiceRequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSaveRequest {

	@NotEmpty(message = "주문 목록은 필수입니다.")
	private List<@NotNull(message = "장바구니 ID는 필수입니다.")
				 @Positive(message = "장바구니 ID는 양수입니다.") Long> cartIdList;

	@Builder
	private OrderSaveRequest(List<Long> cartIdList) {
		this.cartIdList = cartIdList;
	}

	public OrderSaveServiceRequest toServiceRequest() {
		return OrderSaveServiceRequest.builder()
			.cartIdList(cartIdList)
			.build();
	}
}
