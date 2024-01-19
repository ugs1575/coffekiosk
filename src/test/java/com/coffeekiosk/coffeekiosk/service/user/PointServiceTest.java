package com.coffeekiosk.coffeekiosk.service.user;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.service.user.dto.request.PointSaveServiceRequest;

class PointServiceTest extends IntegrationTestSupport {
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
		userRepository.save(user);

		PointSaveServiceRequest request1 = createPointSaveRequest(1000);
		PointSaveServiceRequest request2 = createPointSaveRequest(1000);

		//when
		pointService.savePoint(user.getId(), request1);
		pointService.savePoint(user.getId(), request2);

		//then
		List<User> users = userRepository.findAll();
		assertThat(users).hasSize(1)
			.extracting("id", "point")
			.containsExactlyInAnyOrder(
				tuple(user.getId(), 2000)
			);
	}

	private PointSaveServiceRequest createPointSaveRequest(int point) {
		return PointSaveServiceRequest.builder()
			.amount(point)
			.build();
	}

	private User createUser() {
		return User.builder()
			.name("우경서")
			.build();
	}
}