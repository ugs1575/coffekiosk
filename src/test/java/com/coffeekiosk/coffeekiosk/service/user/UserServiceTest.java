package com.coffeekiosk.coffeekiosk.service.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.service.user.dto.response.UserResponse;

class UserServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

	@DisplayName("사용자 정보를 조회한다.")
	@Test
	void findUserById() {
	    //given
		User user = userRepository.save(createUser("우경서", 10000));

		//when
		UserResponse response = userService.findUser(user.getId());

		//then
		assertThat(response)
			.extracting("name", "point")
			.contains(user.getName(), user.getPoint());

	}

	private User createUser(String name, int point) {
		return User.builder()
			.name(name)
			.point(point)
			.build();
	}

}