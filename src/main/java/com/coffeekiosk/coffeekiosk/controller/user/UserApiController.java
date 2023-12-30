package com.coffeekiosk.coffeekiosk.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.service.user.UserService;
import com.coffeekiosk.coffeekiosk.service.user.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserApiController {

	private final UserService userService;

	@GetMapping("/users/{userId}")
	public ApiResponse<UserResponse> findUser(@PathVariable Long userId) {
		UserResponse response = userService.findUser(userId);
		return ApiResponse.ok(response);
	}
}
