package com.coffeekiosk.coffeekiosk.service.item;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSearchServiceRequest;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemUpdateServiceRequest;
import com.coffeekiosk.coffeekiosk.service.item.dto.response.ItemResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public Long createItem(ItemSaveServiceRequest request, LocalDateTime lastModifiedDateTime) {
		Item item = request.toEntity().create(lastModifiedDateTime);
		Item savedItem = itemRepository.save(item);
		return savedItem.getId();
	}

	@Transactional
	public void updateItem(Long itemId, ItemUpdateServiceRequest request, LocalDateTime updatedModifiedDateTime) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		item.update(request.toEntity(), updatedModifiedDateTime);
	}

	@Transactional
	public void deleteItem(Long itemId) {
		itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		itemRepository.deleteById(itemId);
	}

	public List<ItemResponse> findItems(ItemSearchServiceRequest request, Pageable pageable) {
		return itemRepository.search(request, pageable).getContent();
	}

	public ItemResponse findItem(Long itemId) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		return ItemResponse.of(item);
	}
}
