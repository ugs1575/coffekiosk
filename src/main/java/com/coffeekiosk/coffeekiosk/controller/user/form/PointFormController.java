package com.coffeekiosk.coffeekiosk.controller.user.form;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coffeekiosk.coffeekiosk.controller.item.form.dto.request.ItemSaveForm;
import com.coffeekiosk.coffeekiosk.controller.user.form.dto.request.PointSaveForm;
import com.coffeekiosk.coffeekiosk.facade.RedissonLockPointFacade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PointFormController {

	private final RedissonLockPointFacade pointFacade;

	@GetMapping("/point/save")
	public String savePoint(Model model) {
		model.addAttribute("pointSaveForm", new PointSaveForm());
		return "point/createForm";
	}

	@PostMapping("/point/save")
	public String savePoint(@Valid PointSaveForm pointSaveForm, BindingResult result, RedirectAttributes redirectAttributes) {
		Long userId = 1L;

		if (result.hasErrors()) {
			return "point/createForm";
		}

		pointFacade.savePoint(userId, pointSaveForm.toServiceRequest());
		redirectAttributes.addAttribute("userId", userId);
		return "redirect:/user/{userId}";
	}
}