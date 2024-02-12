package com.coffeekiosk.coffeekiosk.controller.notice.form.dto.request;

import com.coffeekiosk.coffeekiosk.service.notice.request.NoticeSaveUpdateServiceRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeUpdateForm {

	@NotNull(message = "상품 ID는 필수입니다.")
	@Positive(message = "상품 ID는 양수입니다.")
	private Long id;

	@NotBlank(message = "제목은 필수 입니다.")
	private String title;

	private String content;


	@Builder
	private NoticeUpdateForm(String title, String content) {
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
