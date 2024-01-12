package com.coffeekiosk.coffeekiosk.service.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.coffeekiosk.coffeekiosk.service.order.dto.OrderItemSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.OrderSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.response.OrderResponse;

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

		List<Item> items = itemRepository.findAllById(request.getItemIds());
		Map<Long, Item> itemMap = createItemMapBy(items);

		List<OrderItem> orderItems = new ArrayList<>();
		for (OrderItemSaveServiceRequest itemRequest : request.getOrderList()) {
			Item item = findItem(itemMap, itemRequest.getItemId());

			OrderItem orderItem = OrderItem.createOrderItem(item, itemRequest.getCount());
			orderItems.add(orderItem);
		}

		Order order = Order.order(user, orderItems, orderDateTime);

		deductPoint(user, order);

		Order savedOrder = orderRepository.save(order);

		return savedOrder.getId();
	}

	private void deductPoint(User user, Order order) {
		int totalPrice = order.calculateTotalPrice();

		if (user.isMoreThanCurrentPoint(totalPrice)) {
			throw new BusinessException(ErrorCode.INSUFFICIENT_POINT);
		}

		user.deductPoint(order.calculateTotalPrice());
	}

	public OrderResponse findOrder(Long orderId, Long userId) {
		Order order = orderRepository.findByIdFetchJoin(orderId, userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		return OrderResponse.of(order);
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

	private Map<Long, Item> createItemMapBy(List<Item> items) {
		return items.stream()
			.collect(Collectors.toMap(Item::getId, i -> i));
	}
}
