package com.coffeekiosk.coffeekiosk.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.domain.order.Order;
import com.coffeekiosk.coffeekiosk.domain.order.OrderRepository;
import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItem;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.order.dto.OrderItemRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.OrderSaveServiceRequest;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {

	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public Long order(Long userId, OrderSaveServiceRequest request, LocalDateTime orderDateTime) {
		User user = findUser(userId);

		//in 절로 item 불러와서 있는지 확인
		List<Item> items = itemRepository.findAllById(request.getItemIds());
		Map<Long, Item> itemMap = createMapper(items);

		//주문 생성
		Order order = Order.order(user, orderDateTime);

		//주문상품 생성
		for (OrderItemRequest orderItemRequest : request.getOrderItems()) {
			Item item = findItem(itemMap, orderItemRequest.getItemId());

			OrderItem orderItem = OrderItem.createOrderItem(item, orderItemRequest.getCount());
			order.setOrderItem(orderItem);
		}

		Order savedOrder = orderRepository.save(order);

		user.deductPoint(order.calculateTotalPrice());

		return savedOrder.getId();
	}

	private User findUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}

	private Item findItem(Map<Long, Item> itemMap, Long itemId) {
		Item item = itemMap.get(itemId);

		if (item == null) {
			throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		}

		return item;
	}

	private Map<Long, Item> createMapper(List<Item> items) {
		return items.stream()
			.collect(Collectors.toMap(Item::getId, i -> i));
	}
}
