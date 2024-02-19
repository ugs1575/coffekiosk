package com.coffeekiosk.coffeekiosk.facade;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.order.OrderService;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.user.PointService;
import com.coffeekiosk.coffeekiosk.service.user.dto.request.PointSaveServiceRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedissonLockPointFacade {

	private RedissonClient redissonClient;
	private PointService pointService;

	public RedissonLockPointFacade(RedissonClient redissonClient, PointService pointService) {
		this.redissonClient = redissonClient;
		this.pointService = pointService;
	}

	public void savePoint(SessionUser user, PointSaveServiceRequest request) {
		RLock lock = redissonClient.getLock(user.getId().toString());

		try {
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

			if (!available) {
				log.error("lock 획득 실패");
				throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
			}

			pointService.savePoint(user, request);
		} catch (BusinessException e) {
			throw e;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
}
