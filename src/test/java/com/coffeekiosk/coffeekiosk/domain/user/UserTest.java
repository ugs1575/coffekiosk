package com.coffeekiosk.coffeekiosk.domain.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;

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

	@DisplayName("주문시 주문 금액만큼 포인트가 차감된다.")
	@Test
	void deductPoint() {
		//given
		User user = User.builder()
			.point(100)
			.build();

		//when
		user.deductPoint(50);

		//then
		assertThat(user.getPoint()).isEqualTo(50);
	}

	@DisplayName("보유 포인트가 부족할 경우 주문은 불가능하다.")
	@Test
	void insufficientPoint() {
	    //given
		User user = User.builder()
			.point(100)
			.build();

	    //when, then
		assertThatThrownBy(() -> user.deductPoint(200))
			.isInstanceOf(BusinessException.class)
			.hasMessage("현재 가지고 있는 포인트가 주문 금액보다 적습니다.");

	}

}