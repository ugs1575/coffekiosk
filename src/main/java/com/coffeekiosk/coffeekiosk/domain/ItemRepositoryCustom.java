package com.coffeekiosk.coffeekiosk.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coffeekiosk.coffeekiosk.service.dto.request.ItemSearchServiceRequest;
import com.coffeekiosk.coffeekiosk.service.dto.response.ItemResponse;

public interface ItemRepositoryCustom {

	Page<ItemResponse> search(ItemSearchServiceRequest request, Pageable pageable);
}
