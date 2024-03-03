package com.coffeekiosk.coffeekiosk.domain.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSearchServiceRequest;

public interface OrderRepositoryCustom {

	List<Order> findOrders(Long userId, OrderSearchServiceRequest request, Pageable pageable);

	Page<Order> findPageOrders(Long userId, OrderSearchServiceRequest request, Pageable pageable);
}
