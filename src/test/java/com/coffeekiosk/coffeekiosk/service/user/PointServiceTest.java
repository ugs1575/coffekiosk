package com.coffeekiosk.coffeekiosk.service.user;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.domain.user.Role;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.facade.RedissonLockPointFacade;
import com.coffeekiosk.coffeekiosk.service.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.service.user.dto.request.PointSaveServiceRequest;

class PointServiceTest extends IntegrationTestSupport {

	@Autowired
	private RedissonLockPointFacade pointFacade;

	@Autowired
	private PointService pointService;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

	@DisplayName("사용자가 포인트가 적립된다.")
	@Test
	void savePoint() {

		//given
		User user = createUser();
		User savedUser = userRepository.save(user);

		PointSaveServiceRequest request1 = createPointSaveRequest(1000);
		PointSaveServiceRequest request2 = createPointSaveRequest(1000);

		//when
		pointService.savePoint(new SessionUser(savedUser), request1);
		pointService.savePoint(new SessionUser(savedUser), request2);

		//then
		List<User> users = userRepository.findAll();
		assertThat(users).hasSize(1)
			.extracting("id", "point")
			.containsExactlyInAnyOrder(tuple(user.getId(), 2000));
	}

	@DisplayName("같은 사용자 정보로 여러번 포인트 충전을 시도할 때 정상적으로 포인트가 적립된다.")
	@Test
	void orderAtTheSameTime() throws InterruptedException {
		//given
		User user = createUser();
		User savedUser = userRepository.save(user);

		//when
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					PointSaveServiceRequest request = createPointSaveRequest(1);
					pointFacade.savePoint(new SessionUser(savedUser), request);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		//then
		List<User> users = userRepository.findAll();
		assertThat(users.get(0).getPoint()).isEqualTo(100);
	}

	private PointSaveServiceRequest createPointSaveRequest(int point) {
		return PointSaveServiceRequest.builder()
			.amount(point)
			.build();
	}

	private User createUser() {
		return User.builder()
			.email("test@coffeekiosk.com")
			.name("우경서")
			.role(Role.USER)
			.build();
	}
}
