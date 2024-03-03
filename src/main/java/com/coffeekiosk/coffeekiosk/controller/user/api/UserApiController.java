package com.coffeekiosk.coffeekiosk.controller.user.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.config.auth.LoginUser;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.service.user.UserService;
import com.coffeekiosk.coffeekiosk.service.user.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserApiController {

	private final UserService userService;

	@GetMapping("/me")
	public ApiResponse<UserResponse> findUser(@LoginUser SessionUser user) {
		UserResponse response = userService.findUser(user);
		return ApiResponse.ok(response);
	}

	@PostMapping("/role")
	public ApiResponse<UserResponse> updateUserRole(@LoginUser SessionUser user) {
		UserResponse response = userService.updateRole(user);
		return ApiResponse.ok(response);
	}
}
