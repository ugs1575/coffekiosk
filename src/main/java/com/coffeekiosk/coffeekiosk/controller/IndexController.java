package com.coffeekiosk.coffeekiosk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.coffeekiosk.coffeekiosk.config.auth.LoginUser;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.service.user.UserService;
import com.coffeekiosk.coffeekiosk.service.user.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {

	private final UserService userService;

	@GetMapping("/")
	public String index(Model model, @LoginUser SessionUser sessionUser) {
		if (sessionUser != null) {
			UserResponse user = userService.findUser(sessionUser);
			model.addAttribute("user", user);
		}

		return "index";
	}

	@GetMapping("/access-denied")
	public String accessDenied(){
		return "error/accessDenied";
	}
}
