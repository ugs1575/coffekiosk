package com.coffeekiosk.coffeekiosk.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.domain.Item;
import com.coffeekiosk.coffeekiosk.domain.ItemRepository;
import com.coffeekiosk.coffeekiosk.service.dto.request.ItemSaveServiceRequest;
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
}
