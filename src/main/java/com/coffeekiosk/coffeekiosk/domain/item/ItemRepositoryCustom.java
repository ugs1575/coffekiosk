package com.coffeekiosk.coffeekiosk.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSearchServiceRequest;
import com.coffeekiosk.coffeekiosk.service.item.dto.response.ItemResponse;

public interface ItemRepositoryCustom {

	Page<ItemResponse> search(ItemSearchServiceRequest request, Pageable pageable);
}
