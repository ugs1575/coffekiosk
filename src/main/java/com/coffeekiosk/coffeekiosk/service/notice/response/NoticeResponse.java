package com.coffeekiosk.coffeekiosk.service.notice.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.coffeekiosk.coffeekiosk.domain.notice.Notice;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeResponse {

	private Long id;

	private String title;

	private String content;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime registeredDateTime;

	private Long userId;
	
	private String userName;

	@Builder
	public NoticeResponse(Long id, String title, String content, LocalDateTime registeredDateTime, Long userId,
		String userName) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.registeredDateTime = registeredDateTime;
		this.userId = userId;
		this.userName = userName;
	}

	public static NoticeResponse of(Notice notice) {
		return NoticeResponse.builder()
			.id(notice.getId())
			.title(notice.getTitle())
			.content(notice.getContent())
			.registeredDateTime(notice.getRegisteredDateTime())
			.userId(notice.getUser().getId())
			.userName(notice.getUser().getName())
			.build();
	}

	public static List<NoticeResponse> listOf(List<Notice> notices) {
		return notices.stream()
			.map(NoticeResponse::of)
			.collect(Collectors.toList());
	}
}
