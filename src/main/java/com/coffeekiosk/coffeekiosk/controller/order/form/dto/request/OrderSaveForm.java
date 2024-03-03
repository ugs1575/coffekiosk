package com.coffeekiosk.coffeekiosk.controller.order.form.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotEmpty;

import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSaveServiceRequest;

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
		filterNull();
	}

	public void addCartId(Long cartId) {
		cartIdList.add(cartId);
	}

	public OrderSaveServiceRequest toServiceRequest() {
		return OrderSaveServiceRequest.builder()
			.cartIdList(cartIdList)
			.build();
	}

	public void filterNull() {
		cartIdList = cartIdList.stream()
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	public boolean isEmpty() {
		return cartIdList.isEmpty();
	}
}
