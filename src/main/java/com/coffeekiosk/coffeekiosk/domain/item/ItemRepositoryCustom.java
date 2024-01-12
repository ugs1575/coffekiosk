package com.coffeekiosk.coffeekiosk.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSearchServiceRequest;

public interface ItemRepositoryCustom {

	Page<Item> search(ItemSearchServiceRequest request, Pageable pageable);
}
