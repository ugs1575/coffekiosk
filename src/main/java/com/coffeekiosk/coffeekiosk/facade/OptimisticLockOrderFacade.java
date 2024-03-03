package com.coffeekiosk.coffeekiosk.facade;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.service.order.OrderService;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSaveServiceRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OptimisticLockOrderFacade {

	private final OrderService orderService;

	public Long order(Long userId, OrderSaveServiceRequest request, LocalDateTime orderDateTime) throws
		InterruptedException {
		Long orderId = 0L;

		while (true) {
			try {
				orderId = orderService.order(userId, request, orderDateTime);
				break;
			} catch (BusinessException e) {
				throw e;
			} catch (Exception e) {
				Thread.sleep(50);
			}
		}

		return orderId;
	}
}
