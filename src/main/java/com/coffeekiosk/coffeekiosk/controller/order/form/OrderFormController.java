package com.coffeekiosk.coffeekiosk.controller.order.form;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.controller.item.api.dto.request.ItemSearchRequest;
import com.coffeekiosk.coffeekiosk.service.item.ItemService;
import com.coffeekiosk.coffeekiosk.service.item.dto.response.ItemResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class OrderFormController {

	private final ItemService itemService;

	@GetMapping("/menu/list")
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
		model.addAttribute("itemSearchRequest", itemSearchRequest);
		return "menu/listForm";
	}

	@GetMapping("/menu/nextPage")
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
		return "menu/listForm :: #itemList";
	}

	@GetMapping("/menu/{itemId}")
	public String findItem(Model model, @PathVariable Long itemId) {
		try {
			ItemResponse item = itemService.findItem(itemId);
			model.addAttribute("item", item);
			return "menu/detailForm";
		} catch (BusinessException e) {
			return "menu/error";
		}
	}

	private boolean isLastPage(int responseSize, int pageSize) {
		if (responseSize == pageSize + 1) {
			return false;
		}

		return true;
	}
}
