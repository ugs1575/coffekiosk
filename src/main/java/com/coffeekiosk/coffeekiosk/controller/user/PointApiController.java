package com.coffeekiosk.coffeekiosk.controller.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.controller.user.dto.request.PointSaveRequest;
import com.coffeekiosk.coffeekiosk.service.user.PointService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PointApiController {

	private final PointService pointService;

	@PostMapping("/users/{userId}/points")
	public ApiResponse<Void> savePoint(@PathVariable Long userId, @RequestBody @Valid PointSaveRequest request) {
		pointService.savePoint(userId, request.toServiceRequest());
		return ApiResponse.noContent();
	}

}
