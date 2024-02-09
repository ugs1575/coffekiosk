package com.coffeekiosk.coffeekiosk.domain.item;

import java.util.List;

import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSearchServiceRequest;

public interface ItemRepositoryCustom {

	List<Item> search(Long itemId, ItemSearchServiceRequest request, int pageSize);
}