package com.coffeekiosk.coffeekiosk.controller.user.form;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.service.user.UserService;
import com.coffeekiosk.coffeekiosk.service.user.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserFormController {

	private final UserService userService;

	@GetMapping("/user/detail")
	public String findUser(Model model) {
		try {
			Long userId = 1L;
			UserResponse user = userService.findUser(userId);
			model.addAttribute("user", user);
			return "user/detailForm";
		} catch (BusinessException e) {
			return "user/error";
		}
	}
}
