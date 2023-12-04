package com.coffeekiosk.coffeekiosk.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.common.dto.response.ApiResponse;
import com.coffeekiosk.coffeekiosk.controller.dto.request.ItemSaveRequest;
import com.coffeekiosk.coffeekiosk.controller.dto.request.ItemUpdateRequest;
import com.coffeekiosk.coffeekiosk.service.ItemService;
import com.coffeekiosk.coffeekiosk.service.dto.response.ItemResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ItemApiController {

	private final ItemService itemService;

	@PostMapping("/items")
	public ApiResponse<ItemResponse> createItem(@RequestBody @Valid ItemSaveRequest request) {
		ItemResponse response = itemService.createItem(request.toServiceRequest(), LocalDateTime.now());
		return ApiResponse.ok(response);
	}

	@PatchMapping("/items/{itemId}")
	public ApiResponse<ItemResponse> updateItem(@PathVariable Long itemId, @RequestBody @Valid ItemUpdateRequest request) {
		ItemResponse response = itemService.updateItem(itemId, request.toServiceRequest(), LocalDateTime.now());
		return ApiResponse.ok(response);
	}

	@DeleteMapping("/items/{itemId}")
	public ApiResponse<Void> updateItem(@PathVariable Long itemId) {
		itemService.deleteItem(itemId);
		return ApiResponse.noContent();
	}

	@GetMapping("/items")
	public ApiResponse<List<ItemResponse>> findItems(@PageableDefault(value = 10) Pageable pageable) {
		List<ItemResponse> response = itemService.findItems(pageable);
		return ApiResponse.ok(response);
	}

}
