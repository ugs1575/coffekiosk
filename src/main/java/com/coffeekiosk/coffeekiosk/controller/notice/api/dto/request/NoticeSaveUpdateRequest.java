package com.coffeekiosk.coffeekiosk.controller.notice.api.dto.request;

import com.coffeekiosk.coffeekiosk.service.notice.request.NoticeSaveUpdateServiceRequest;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class
NoticeSaveUpdateRequest {

	@NotBlank(message = "제목은 필수 입니다.")
	private String title;

	private String content;


	@Builder
	private NoticeSaveUpdateRequest(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public NoticeSaveUpdateServiceRequest toServiceRequest() {
		return NoticeSaveUpdateServiceRequest.builder()
			.title(title)
			.content(content)
			.build();
	}
}
