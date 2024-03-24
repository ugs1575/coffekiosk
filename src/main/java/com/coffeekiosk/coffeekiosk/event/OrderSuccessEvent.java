package com.coffeekiosk.coffeekiosk.event;

import com.coffeekiosk.coffeekiosk.domain.order.Order;
import com.coffeekiosk.coffeekiosk.domain.user.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderSuccessEvent {
	private final Order order;
	private final User user;
}
