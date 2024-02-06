package com.coffeekiosk.coffeekiosk.controller.item.form;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.controller.item.api.dto.request.ItemSearchRequest;
import com.coffeekiosk.coffeekiosk.controller.item.form.dto.request.ItemSaveForm;
import com.coffeekiosk.coffeekiosk.service.item.ItemService;
import com.coffeekiosk.coffeekiosk.service.item.dto.response.ItemResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ItemFormController {

	private final ItemService itemService;

	@GetMapping("/item/list")
	public String list(
		Model model,
		ItemSearchRequest itemSearchRequest,
		@RequestParam(value = "pageSize", defaultValue = "10") int pageSize
	) {
		List<ItemResponse> items = itemService.findItems(null, itemSearchRequest.toServiceRequest(), pageSize + 1);
		boolean lastPage = isLastPage(items.size(), pageSize);

		if (!lastPage) {
			items.remove(items.size() - 1);
		}

		model.addAttribute("items", items);
		model.addAttribute("lastPage", lastPage);
		return "item/listForm";
	}

	@GetMapping("/item/nextPage")
	public String nextPage(
		Model model,
		Long itemId,
		ItemSearchRequest itemSearchRequest,
		@RequestParam(value = "pageSize", defaultValue = "10") int pageSize
	) {
		List<ItemResponse> items = itemService.findItems(itemId, itemSearchRequest.toServiceRequest(), pageSize + 1);
		boolean lastPage = isLastPage(items.size(), pageSize);

		if (!lastPage) {
			items.remove(items.size() - 1);
		}

		model.addAttribute("items", items);
		model.addAttribute("lastPage", lastPage);
		return "item/listForm :: #itemList";
	}

	@GetMapping("/item/{itemId}")
	public String findItem(Model model, @PathVariable Long itemId) {
		try {
			ItemResponse item = itemService.findItem(itemId);
			model.addAttribute("item", item);
			model.addAttribute("status", true);
		} catch (BusinessException e) {
			model.addAttribute("status", false);
		}

		return "item/detailForm";
	}

	@GetMapping("/item/new")
	public String createForm(Model model) {
		model.addAttribute("itemSaveForm", new ItemSaveForm());
		return "item/createForm";
	}

	@PostMapping("/item/new")
	public String create(@Valid ItemSaveForm itemSaveForm, BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "item/createForm";
		}

		Long itemId = itemService.createItem(itemSaveForm.toServiceRequest(), LocalDateTime.now());
		redirectAttributes.addAttribute("itemId", itemId);
		redirectAttributes.addAttribute("status", true);
		return "redirect:/item/{itemId}";
	}

	private boolean isLastPage(int responseSize, int pageSize) {
		if (responseSize == pageSize + 1) {
			return false;
		}

		return true;
	}
}
