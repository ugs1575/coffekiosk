package com.coffeekiosk.coffeekiosk.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.user.dto.request.PointSaveServiceRequest;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PointService {

	private final UserRepository userRepository;

	@Transactional
	public void savePoint(SessionUser sessionUser, PointSaveServiceRequest request) {
		User user = findUser(sessionUser);
		user.savePoint(request.getAmount());
	}

	private User findUser(SessionUser sessionUser) {
		return userRepository.findById(sessionUser.getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}
}
