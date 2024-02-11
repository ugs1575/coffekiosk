package com.coffeekiosk.coffeekiosk.controller.order.form.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSaveServiceRequest;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class OrderSaveForm {

	@NotEmpty(message = "주문 목록은 필수입니다.")
	private List<Long> cartIdList;

	public OrderSaveForm() {
		cartIdList = new ArrayList<>();
	}

	public OrderSaveForm(List<Long> cartIdList) {
		this.cartIdList = cartIdList;
	}

	public void addCartId(Long cartId) {
		cartIdList.add(cartId);
	}

	public void filterNull() {
		cartIdList = cartIdList.stream()
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	public OrderSaveServiceRequest toServiceRequest() {
		return OrderSaveServiceRequest.builder()
			.cartIdList(cartIdList)
			.build();
	}
}
