package com.coffeekiosk.coffeekiosk.service.notice.request;

import java.time.LocalDateTime;

import com.coffeekiosk.coffeekiosk.domain.notice.Notice;
import com.coffeekiosk.coffeekiosk.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeSaveUpdateServiceRequest {

	private String title;
	private String content;

	@Builder
	private NoticeSaveUpdateServiceRequest(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public Notice toEntity(User user, LocalDateTime registeredDateTime) {
		return Notice.builder()
			.user(user)
			.title(title)
			.content(content)
			.registeredDateTime(registeredDateTime)
			.build();
	}
}
