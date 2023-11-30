package com.coffeekiosk.coffeekiosk.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.Item;
import com.coffeekiosk.coffeekiosk.domain.ItemRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.dto.request.ItemSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.dto.request.ItemUpdateServiceRequest;
import com.coffeekiosk.coffeekiosk.service.dto.response.ItemResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public ItemResponse createItem(ItemSaveServiceRequest request, LocalDateTime lastModifiedDateTime) {
		Item item = request.toEntity().create(lastModifiedDateTime);
		Item savedItem = itemRepository.save(item);
		return ItemResponse.of(savedItem);
	}

	@Transactional
	public ItemResponse updateItem(Long itemId, ItemUpdateServiceRequest request, LocalDateTime updatedModifiedDateTime) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		Item updatedItem = item.update(request.toEntity(), updatedModifiedDateTime);
		return ItemResponse.of(updatedItem);
	}

	@Transactional
	public void deleteItem(Long itemId) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		itemRepository.deleteById(itemId);
	}
}
