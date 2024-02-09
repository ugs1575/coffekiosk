package com.coffeekiosk.coffeekiosk.service.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.cart.Cart;
import com.coffeekiosk.coffeekiosk.domain.cart.CartRepository;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.domain.order.Order;
import com.coffeekiosk.coffeekiosk.domain.order.OrderRepository;
import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItem;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSaveServiceRequest;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;

	public Long order(Long userId, OrderSaveServiceRequest request, LocalDateTime orderDateTime) {
		User user = findUser(userId);

		List<Cart> cartList = cartRepository.findAllByIdFetchJoin(request.getCartIdList(), userId);
		if (cartList.isEmpty()) {
			throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		}

		List<OrderItem> orderItems = new ArrayList<>();
		for (Cart cart : cartList) {
			Item item = cart.getItem();
			OrderItem orderItem = OrderItem.createOrderItem(item, cart.getCount());
			orderItems.add(orderItem);
		}

		Order order = Order.order(user, orderItems, orderDateTime);

		deductPoint(user, order);

		Order savedOrder = orderRepository.save(order);

		cartRepository.deleteByIdIn(request.getCartIdList(), userId);

		return savedOrder.getId();
	}

	private void deductPoint(User user, Order order) {
		int totalPrice = order.calculateTotalPrice();

		if (user.isMoreThanCurrentPoint(totalPrice)) {
			throw new BusinessException(ErrorCode.INSUFFICIENT_POINT);
		}

		user.deductPoint(order.calculateTotalPrice());
	}

	private User findUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}
}
