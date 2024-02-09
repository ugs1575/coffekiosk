package com.coffeekiosk.coffeekiosk.controller.order.dto.request;

import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderItemSaveServiceRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemSaveRequest {

	@NotNull(message = "상품 아이디는 필수입니다.")
	@Positive(message = "상품 아이디는 양수입니다.")
	private Long itemId;

	@Min(value = 1, message = "최소 주문 상품 수는 1개 이상이어야 합니다.")
	private int count;

	@Builder
	private OrderItemSaveRequest(Long itemId, int count) {
		this.itemId = itemId;
		this.count = count;
	}

	public OrderItemSaveServiceRequest toServiceRequest() {
		return OrderItemSaveServiceRequest.builder()
			.itemId(itemId)
			.count(count)
			.build();
	}
}
