package com.coffeekiosk.coffeekiosk.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.domain.user.Role;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.user.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public UserResponse updateRole(SessionUser sessionUser) {
		User user = findById(sessionUser.getId());
		if (user.getRole().equals(Role.USER)) {
			user.updateRole(Role.ADMIN);
		} else {
			user.updateRole(Role.USER);
		}

		return UserResponse.of(user);
	}

	public UserResponse findUser(SessionUser sessionUser) {
		User user = findById(sessionUser.getId());
		return UserResponse.of(user);
	}

	private User findById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}
}
