package com.coffeekiosk.coffeekiosk.controller.user.form;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.config.auth.LoginUser;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.service.user.UserService;
import com.coffeekiosk.coffeekiosk.service.user.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserFormController {

	private final UserService userService;

	@GetMapping("/user/detail")
	public String findUser(Model model, @LoginUser SessionUser sessionUser) {
		try {
			UserResponse user = userService.findUser(sessionUser);
			model.addAttribute("user", user);
			return "user/detailForm";
		} catch (BusinessException e) {
			return "user/error";
		}
	}
}
