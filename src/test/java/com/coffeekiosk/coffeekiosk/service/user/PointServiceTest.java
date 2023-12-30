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
		PointSaveServiceRequest request1 = PointSaveServiceRequest.builder()
			.amount(10000)
			.build();
		PointSaveServiceRequest request2 = PointSaveServiceRequest.builder()
			.amount(1000)
			.build();

		//when
		pointService.savePoint(user.getId(), request1);
		pointService.savePoint(user.getId(), request2);

		//then
		List<User> users = userRepository.findAll();
		User savedUser = users.get(0);
		assertThat(savedUser.getPoint()).isEqualTo(11000);
	}

	private User createUser() {
		return userRepository.save(User.builder()
			.name("우경서")
			.build());
	}
}