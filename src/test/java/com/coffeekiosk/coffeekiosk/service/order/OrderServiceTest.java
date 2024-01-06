package com.coffeekiosk.coffeekiosk.service.order;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.domain.order.OrderRepository;
import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItemRepository;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.controller.order.dto.request.OrderItemSaveRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.OrderItemSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.order.dto.OrderSaveServiceRequest;

class OrderServiceTest extends IntegrationTestSupport {

	@Autowired
	private OrderService orderService;

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


	@DisplayName("상품을 주문한다.")
	@Test
	void createOrder() {
	    //given
		LocalDateTime orderDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		User user = createUser(20000);
		userRepository.save(user);

		Item item1 = createItem("카페라떼", 5000);
		Item item2 = createItem("아메리카노", 4500);
		itemRepository.saveAll(List.of(item1, item2));

		OrderItemSaveServiceRequest request1 = createOrderItemRequest(item1, 1);
		OrderItemSaveServiceRequest request2 = createOrderItemRequest(item2, 2);
		OrderSaveServiceRequest request = OrderSaveServiceRequest.builder()
			.orderItems(List.of(request1, request2))
			.build();

		//when
		Long orderId = orderService.order(user.getId(), request, orderDateTime);

		//then
		assertThat(orderId).isNotNull();

		List<User> users = userRepository.findAll();
		assertThat(users).hasSize(1)
			.extracting("id", "point")
			.containsExactlyInAnyOrder(
				tuple(user.getId(), 6000)
			);
	}

	@DisplayName("주문 금액보다 보유 포인트가 클 경우 예외가 발생한다.")
	@Test
	void createOrderWithInsufficientPoint() {
		//given
		LocalDateTime orderDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		User user = createUser(4000);
		userRepository.save(user);

		Item item = createItem("카페라떼", 5000);
		itemRepository.save(item);

		OrderItemSaveServiceRequest orderItemRequest = createOrderItemRequest(item, 1);
		OrderSaveServiceRequest request = OrderSaveServiceRequest.builder()
			.orderItems(List.of(orderItemRequest))
			.build();

		//when //then
		assertThatThrownBy(() -> orderService.order(user.getId(), request, orderDateTime))
			.isInstanceOf(BusinessException.class)
			.hasMessage("현재 가지고 있는 포인트가 주문 금액보다 적습니다.");

	}

	private OrderItemSaveServiceRequest createOrderItemRequest(Item item, int count) {
		return OrderItemSaveServiceRequest.builder()
			.itemId(item.getId())
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