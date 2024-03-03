package com.coffeekiosk.coffeekiosk.service.user;

import static com.coffeekiosk.coffeekiosk.domain.user.Role.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.domain.user.Role;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.service.IntegrationTestSupport;
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

	@DisplayName("사용자를 관리자로 전환한다.")
	@Test
	void updateRoleToAdmin() {
		//given
		User user = userRepository.save(createUser(USER));

		//when
		UserResponse userResponse = userService.updateRole(new SessionUser(user));

		//then
		assertThat(userResponse.getRole()).isEqualTo(ADMIN);
	}

	@DisplayName("관리자를 일반 사용자로 전환한다.")
	@Test
	void updateRoleToUser() {
		//given
		User user = userRepository.save(createUser(ADMIN));

		//when
		UserResponse userResponse = userService.updateRole(new SessionUser(user));

		//then
		assertThat(userResponse.getRole()).isEqualTo(USER);
	}

	@DisplayName("사용자 정보를 조회한다.")
	@Test
	void findUserById() {
		//given
		User user = userRepository.save(createUser(USER));

		//when
		UserResponse response = userService.findUser(new SessionUser(user));

		//then
		assertThat(response)
			.extracting("name", "point")
			.contains(user.getName(), user.getPoint());

	}

	private User createUser(Role role) {
		return User.builder()
			.name("우경서")
			.email("test@coffeekiosk.com")
			.role(role)
			.point(10000)
			.build();
	}

}
