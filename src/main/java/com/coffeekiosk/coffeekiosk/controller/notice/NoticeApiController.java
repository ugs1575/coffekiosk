package com.coffeekiosk.coffeekiosk.controller.notice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.controller.notice.dto.request.NoticeSaveUpdateRequest;
import com.coffeekiosk.coffeekiosk.service.notice.NoticeService;
import com.coffeekiosk.coffeekiosk.service.notice.response.NoticeResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class NoticeApiController {

	private final NoticeService noticeService;

	@PostMapping("/users/{userId}/notices")
	public ResponseEntity<ApiResponse<Void>> createNotice(@PathVariable Long userId, @RequestBody @Valid NoticeSaveUpdateRequest request) {
		NoticeResponse response = noticeService.createNotice(userId, request.toServiceRequest(), LocalDateTime.now());
		return ResponseEntity.created(URI.create("/api/notices/" + response.getId())).body(ApiResponse.created());
	}

	@PatchMapping("/notices/{noticeId}")
	public ApiResponse<Void> updateNotice(@PathVariable Long noticeId, @RequestBody @Valid NoticeSaveUpdateRequest request) {
		noticeService.updateNotice(noticeId, request.toServiceRequest());
		return ApiResponse.noContent();
	}

	@GetMapping("/notices/{noticeId}")
	public ApiResponse<NoticeResponse> findNotice(@PathVariable Long noticeId) {
		NoticeResponse response = noticeService.findById(noticeId);
		return ApiResponse.ok(response);
	}

	@GetMapping("/notices")
	public ApiResponse<List<NoticeResponse>> findNotices() {
		List<NoticeResponse> response = noticeService.findNotices();
		return ApiResponse.ok(response);
	}

	@DeleteMapping("/notices/{noticeId}")
	public ApiResponse<Void> deleteNotice(@PathVariable Long noticeId) {
		noticeService.delete(noticeId);
		return ApiResponse.noContent();
	}
}