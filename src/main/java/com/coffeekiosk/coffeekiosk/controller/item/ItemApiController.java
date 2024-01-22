package com.coffeekiosk.coffeekiosk.controller.item;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.common.dto.response.CreatedResponse;
import com.coffeekiosk.coffeekiosk.controller.item.dto.request.ItemSaveRequest;
import com.coffeekiosk.coffeekiosk.controller.item.dto.request.ItemSearchRequest;
import com.coffeekiosk.coffeekiosk.controller.item.dto.request.ItemUpdateRequest;
import com.coffeekiosk.coffeekiosk.service.item.ItemService;
import com.coffeekiosk.coffeekiosk.service.item.dto.response.ItemResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/items")
@RequiredArgsConstructor
@RestController
public class ItemApiController {

	private final ItemService itemService;

	@PostMapping
	public ApiResponse<CreatedResponse> createItem(@RequestBody @Valid ItemSaveRequest request) {
		Long itemId = itemService.createItem(request.toServiceRequest(), LocalDateTime.now());
		return ApiResponse.created(itemId);
	}

	@PatchMapping("/{itemId}")
	public ApiResponse<Void> updateItem(@PathVariable Long itemId, @RequestBody @Valid ItemUpdateRequest request) {
		itemService.updateItem(itemId, request.toServiceRequest(), LocalDateTime.now());
		return ApiResponse.noContent();
	}

	@DeleteMapping("/{itemId}")
	public ApiResponse<Void> deleteItem(@PathVariable Long itemId) {
		itemService.deleteItem(itemId);
		return ApiResponse.noContent();
	}

	@GetMapping
	public ApiResponse<List<ItemResponse>> findItems(ItemSearchRequest itemSearchRequest, @PageableDefault(value = 10) Pageable pageable) {
		List<ItemResponse> response = itemService.findItems(itemSearchRequest.toServiceRequest(), pageable);
		return ApiResponse.ok(response);
	}

	@GetMapping("/{itemId}")
	public ApiResponse<ItemResponse> findItem(@PathVariable Long itemId) {
		ItemResponse response = itemService.findItem(itemId);
		return ApiResponse.ok(response);
	}

}
