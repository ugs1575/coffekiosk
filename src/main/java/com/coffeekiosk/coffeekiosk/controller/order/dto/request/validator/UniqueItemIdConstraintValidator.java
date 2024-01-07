package com.coffeekiosk.coffeekiosk.controller.order.dto.request.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coffeekiosk.coffeekiosk.controller.order.dto.request.OrderItemSaveRequest;
import com.coffeekiosk.coffeekiosk.controller.order.dto.request.OrderSaveRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueItemIdConstraintValidator implements ConstraintValidator<UniqueItemIdConstraint, List<OrderItemSaveRequest>> {
	@Override
	public boolean isValid(List<OrderItemSaveRequest> values, ConstraintValidatorContext context) {
		Set<Long> items = new HashSet<>();

		for (OrderItemSaveRequest request : values) {
			if (!items.add(request.getItemId())) {
				return false;
			}
		}

		return true;
	}
}
