package com.coffeekiosk.coffeekiosk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.coffeekiosk.coffeekiosk.config.auth.LoginUser;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {

	private final HttpSession httpSession;

	@GetMapping("/")
	public String index(Model model, @LoginUser SessionUser user) {
		if (user != null) {
			model.addAttribute("userName", user.getName());
		}

		return "index";
	}
}
