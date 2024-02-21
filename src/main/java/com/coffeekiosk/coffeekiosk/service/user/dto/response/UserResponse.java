package com.coffeekiosk.coffeekiosk.service.user.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.coffeekiosk.coffeekiosk.domain.user.Role;
import com.coffeekiosk.coffeekiosk.domain.user.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

	private Long id;

	private String name;

	private int point;

	private Role role;

	@Builder
	private UserResponse(Long id, String name, int point, Role role) {
		this.id = id;
		this.name = name;
		this.point = point;
		this.role = role;
	}

	public static UserResponse of(User user) {
		return UserResponse.builder()
			.id(user.getId())
			.name(user.getName())
			.point(user.getPoint())
			.role(user.getRole())
			.build();
	}

	public static List<UserResponse> listOf(List<User> users) {
		return users.stream()
			.map(UserResponse::of)
			.collect(Collectors.toList());
	}
}
