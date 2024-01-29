package com.coffeekiosk.coffeekiosk.service.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.coffeekiosk.coffeekiosk.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.domain.order.Order;
import com.coffeekiosk.coffeekiosk.domain.order.OrderRepository;
import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItem;
import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItemRepository;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSearchServiceRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.response.OrderResponse;

class OrderHistoryServiceTest extends IntegrationTestSupport {

	@Autowired
	private OrderHistoryService orderHistoryService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		orderItemRepository.deleteAllInBatch();
		itemRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}


	@DisplayName("주문 상세 정보를 조회한다.")
	@Test
	void findOrderById() {
		//given
		User user = createUser(10000);
		userRepository.save(user);

		Item item1 = createItem("카페라떼", 5000);
		Item item2 = createItem("아메리카노", 4500);
		itemRepository.saveAll(List.of(item1, item2));

		OrderItem orderItem1 = createOrderItem(item1, 1);
		OrderItem orderItem2 = createOrderItem(item2, 1);

		LocalDateTime orderDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		Order order = createOrder(user, List.of(orderItem1, orderItem2), orderDateTime);
		Order savedOrder = orderRepository.save(order);

		//when
		OrderResponse orderResponse = orderHistoryService.findOrder(order.getId(), user.getId());

		//then
		assertThat(orderResponse)
			.extracting("id", "totalPrice", "orderDateTime")
			.contains(savedOrder.getId(), 9500, orderDateTime);

		assertThat(orderResponse.getOrderItems())
			.extracting( "itemId", "itemName", "itemPrice", "count", "orderPrice")
			.containsExactlyInAnyOrder(
				tuple(item1.getId(), item1.getName(), item1.getPrice(), orderItem1.getCount(), orderItem1.getOrderPrice()),
				tuple(item2.getId(), item2.getName(), item2.getPrice(), orderItem2.getCount(), orderItem2.getOrderPrice())
			);

	}

	@DisplayName("주어진 검색 기간 내 주문 목록을 페이징 하여 최신 순으로 조회한다.")
	@Test
	void findPagedOrders() {
		//given
		User user = createUser(30000);
		userRepository.save(user);

		Item item = createItem("카페라떼", 5000);
		itemRepository.save(item);

		LocalDateTime orderDateTime1 = LocalDateTime.of(2023, 11, 21, 0, 0);
		OrderItem orderItem1 = createOrderItem(item, 1);
		Order order1 = createOrder(user, List.of(orderItem1), orderDateTime1);

		LocalDateTime orderDateTime2 = LocalDateTime.of(2023, 12, 1, 0, 0);
		OrderItem orderItem2 = createOrderItem(item, 1);
		Order order2 = createOrder(user, List.of(orderItem2), orderDateTime2);

		LocalDateTime orderDateTime3 = LocalDateTime.of(2023, 12, 21, 0, 0);
		OrderItem orderItem3 = createOrderItem(item, 1);
		Order order3 = createOrder(user, List.of(orderItem3), orderDateTime3);

		orderRepository.saveAll(List.of(order1, order2, order3));

		OrderSearchServiceRequest request = OrderSearchServiceRequest.builder()
			.startDate(orderDateTime1)
			.endDate(orderDateTime2)
			.build();

		PageRequest pageRequest = PageRequest.of(0, 3);

		//when
		List<OrderResponse> orderResponses = orderHistoryService.findOrders(user.getId(), request, pageRequest);

		//then
		assertThat(orderResponses)
			.extracting("id", "orderDateTime")
			.containsExactly(
				tuple(order2.getId(), orderDateTime2),
				tuple(order1.getId(), orderDateTime1)
			);
	}

	private Order createOrder(User user, List<OrderItem> orderItems, LocalDateTime orderDateTime) {
		return Order.builder()
			.user(user)
			.orderItems(orderItems)
			.orderDateTime(orderDateTime)
			.build();
	}

	private OrderItem createOrderItem(Item item, int count) {
		return OrderItem.builder()
			.item(item)
			.count(count)
			.build();
	}

	private Item createItem(String name, int price) {
		return Item.builder()
			.name(name)
			.itemType(ItemType.COFFEE)
			.price(price)
			.lastModifiedDateTime(LocalDateTime.now())
			.build();
	}

	private User createUser(int point) {
		return User.builder()
			.name("우경서")
			.point(point)
			.build();
	}

}