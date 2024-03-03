package com.coffeekiosk.coffeekiosk.domain.notice;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.coffeekiosk.coffeekiosk.domain.user.User;

class NoticeTest {

	@DisplayName("공지사항 내용을 수정한다.")
	@Test
	void updateNotice() {

		//given
		User user = createUser();

		LocalDateTime registeredDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Notice notice = createNotice(user, registeredDateTime);

		//when
		notice.update("테스트 공지 수정 제목", "테스트 공지 수정 내용");

		//then
		assertThat(notice.getTitle()).isEqualTo("테스트 공지 수정 제목");
		assertThat(notice.getContent()).isEqualTo("테스트 공지 수정 내용");
	}

	private Notice createNotice(User user, LocalDateTime registeredDateTime) {
		return Notice.builder()
			.user(user)
			.title("테스트 공지 제목")
			.content("테스트 공지 내용")
			.registeredDateTime(registeredDateTime)
			.build();
	}

	private User createUser() {
		return User.builder()
			.name("우경서")
			.build();
	}
}
