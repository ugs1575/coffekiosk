package com.coffeekiosk.coffeekiosk.controller.notice.form;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.config.auth.LoginUser;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.controller.notice.form.dto.request.NoticeSaveForm;
import com.coffeekiosk.coffeekiosk.controller.notice.form.dto.request.NoticeUpdateForm;
import com.coffeekiosk.coffeekiosk.service.notice.NoticeService;
import com.coffeekiosk.coffeekiosk.service.notice.response.NoticeResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class NoticeFormController {

	private final NoticeService noticeService;

	@GetMapping("/notice/list")
	public String list(Model model) {
		List<NoticeResponse> notices = noticeService.findNotices();
		model.addAttribute("notices", notices);
		return "notice/listForm";
	}

	@GetMapping("/notice/{noticeId}")
	public String findItem(Model model, @PathVariable Long noticeId) {
		try {
			NoticeResponse notice = noticeService.findById(noticeId);
			model.addAttribute("notice", notice);
			return "notice/detailForm";
		} catch (BusinessException e) {
			return "notice/error";
		}
	}

	@PostMapping("/notice/{noticeId}/delete")
	public String delete(@PathVariable Long noticeId) {
		noticeService.delete(noticeId);
		return "redirect:/notice/list";
	}

	@GetMapping("/notice/new")
	public String create(Model model) {
		model.addAttribute("noticeSaveForm", new NoticeSaveForm());
		return "notice/createForm";
	}

	@PostMapping("/notice/new")
	public String create(
		@LoginUser SessionUser sessionUser,
		@Valid NoticeSaveForm noticeSaveForm,
		BindingResult result,
		RedirectAttributes redirectAttributes
	) {
		if (result.hasErrors()) {
			return "notice/createForm";
		}

		NoticeResponse notice = noticeService.createNotice(sessionUser, noticeSaveForm.toServiceRequest(),
			LocalDateTime.now());
		redirectAttributes.addAttribute("noticeId", notice.getId());
		return "redirect:/notice/{noticeId}";
	}

	@GetMapping("/notice/{noticeId}/edit")
	public String update(Model model, @PathVariable Long noticeId) {
		try {
			NoticeResponse notice = noticeService.findById(noticeId);
			model.addAttribute("noticeUpdateForm", notice);
			return "notice/updateForm";
		} catch (BusinessException e) {
			return "notice/error";
		}
	}

	@PostMapping("/notice/{noticeId}/edit")
	public String update(Model model, @PathVariable Long noticeId,
		@Valid NoticeUpdateForm noticeUpdateForm, BindingResult result,
		RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("noticeUpdateForm", noticeUpdateForm);
			return "notice/updateForm";
		}

		NoticeResponse response = noticeService.updateNotice(noticeId, noticeUpdateForm.toServiceRequest());
		redirectAttributes.addAttribute("noticeId", response.getId());
		return "redirect:/notice/{noticeId}";
	}

}
