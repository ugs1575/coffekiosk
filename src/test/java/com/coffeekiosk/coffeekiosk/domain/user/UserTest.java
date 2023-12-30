package com.coffeekiosk.coffeekiosk.domain.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

	@DisplayName("포인트를 충전 시 포인트가 적립된다.")
	@Test
	void savePoint() {
	    //given
		User user = new User();

	    //when
		user.savePoint(1000);
		user.savePoint(1000);

	    //then
		assertThat(user.getPoint()).isEqualTo(2000);

	}

}