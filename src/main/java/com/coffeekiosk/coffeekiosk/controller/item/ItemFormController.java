package com.coffeekiosk.coffeekiosk.controller.item;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coffeekiosk.coffeekiosk.controller.item.dto.request.ItemSearchRequest;
import com.coffeekiosk.coffeekiosk.service.item.ItemService;
import com.coffeekiosk.coffeekiosk.service.item.dto.response.ItemResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	private boolean isLastPage(int responseSize, int pageSize) {
		if (responseSize == pageSize + 1) {
			return false;
		}

		return true;
	}
}
