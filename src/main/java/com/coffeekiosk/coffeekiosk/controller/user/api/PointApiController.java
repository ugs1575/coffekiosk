package com.coffeekiosk.coffeekiosk.controller.user.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.config.auth.LoginUser;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.controller.user.api.dto.request.PointSaveRequest;
import com.coffeekiosk.coffeekiosk.service.user.PointService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/points")
@RequiredArgsConstructor
@RestController
public class PointApiController {

	private final PointService pointService;

	@PostMapping
	public ApiResponse<Void> savePoint(@LoginUser SessionUser user, @RequestBody @Valid PointSaveRequest request) {
		pointService.savePoint(user, request.toServiceRequest());
		return ApiResponse.noContent();
	}

}
