package com.coffeekiosk.coffeekiosk.facade;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.order.OrderService;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSaveServiceRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedissonLockOrderFacade {

	private RedissonClient redissonClient;
	private OrderService orderService;

	public RedissonLockOrderFacade(RedissonClient redissonClient, OrderService orderService) {
		this.redissonClient = redissonClient;
		this.orderService = orderService;
	}

	public Long order(Long userId, OrderSaveServiceRequest request, LocalDateTime orderDateTime) {
		RLock lock = redissonClient.getLock(userId.toString());

		try {
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

			if (!available) {
				log.error("lock 획득 실패");
				throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
			}

			return orderService.order(userId, request, orderDateTime);
		} catch (BusinessException e) {
			throw e;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
}
