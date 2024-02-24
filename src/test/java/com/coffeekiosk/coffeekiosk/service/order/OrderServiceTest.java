package com.coffeekiosk.coffeekiosk.service.order;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.service.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.cart.Cart;
import com.coffeekiosk.coffeekiosk.domain.cart.CartRepository;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.domain.order.OrderRepository;
import com.coffeekiosk.coffeekiosk.domain.orderitem.OrderItemRepository;
import com.coffeekiosk.coffeekiosk.domain.user.Role;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.facade.RedissonLockOrderFacade;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSaveServiceRequest;

class OrderServiceTest extends IntegrationTestSupport {

	@Autowired
	private RedissonLockOrderFacade orderFacade;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@AfterEach
	void tearDown() {
		cartRepository.deleteAllInBatch();
		orderItemRepository.deleteAllInBatch();
		itemRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}


	@DisplayName("상품을 주문한다.")
	@Test
	void createOrder() throws InterruptedException {
	    //given
		LocalDateTime orderDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		User user = createUser(20000);
		User savedUser = userRepository.save(user);

		Item item1 = createItem("카페라떼", 5000);
		Item savedItem1 = itemRepository.save(item1);
		Item item2 = createItem("아메리카노", 4500);
		Item savedItem2 = itemRepository.save(item2);

		Cart cart1 = createCart(savedUser, savedItem1, 1);
		Cart savedCart1 = cartRepository.save(cart1);
		Cart cart2 = createCart(savedUser, savedItem2, 2);
		Cart savedCart2 = cartRepository.save(cart2);

		OrderSaveServiceRequest request = OrderSaveServiceRequest.builder()
			.cartIdList(List.of(savedCart1.getId(), savedCart2.getId()))
			.build();

		//when
		Long orderId = orderFacade.order(new SessionUser(savedUser), request, orderDateTime);

		//then
		assertThat(orderId).isNotNull();

		List<User> users = userRepository.findAll();
		assertThat(users).hasSize(1)
			.extracting("id", "point")
			.containsExactlyInAnyOrder(
				tuple(savedUser.getId(), 6000)
			);

		List<Cart> carts = cartRepository.findAll();
		assertThat(carts).hasSize(0)
			.isEmpty();
	}

	@DisplayName("주문 금액보다 보유 포인트가 클 경우 예외가 발생한다.")
	@Test
	void createOrderWithInsufficientPoint() {
		//given
		LocalDateTime orderDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		User user = createUser(4000);
		User savedUser = userRepository.save(user);

		Item item = createItem("카페라떼", 5000);
		Item savedItem = itemRepository.save(item);

		Cart cart = createCart(savedUser, savedItem, 1);
		Cart savedCart = cartRepository.save(cart);

		OrderSaveServiceRequest request = OrderSaveServiceRequest.builder()
			.cartIdList(List.of(savedCart.getId()))
			.build();

		//when //then
		assertThatThrownBy(() -> orderFacade.order(new SessionUser(savedUser), request, orderDateTime))
			.isInstanceOf(BusinessException.class)
			.hasMessage("현재 가지고 있는 포인트가 주문 금액보다 적습니다.");

	}

	@DisplayName("같은 사용자 정보로 여러개의 주문이 동시에 들어올 때 정상적으로 포인트가 차감된다.")
	@Test
	void orderAtTheSameTime() throws InterruptedException {
		//given
		User user = createUser(100);
		User savedUser = userRepository.save(user);

		Item item = createItem("카페라떼", 1);
		Item savedItem = itemRepository.save(item);

		//when
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					LocalDateTime orderDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
					Cart cart = createCart(savedUser, savedItem, 1);
					Cart savedCart = cartRepository.save(cart);

					OrderSaveServiceRequest request = OrderSaveServiceRequest.builder()
						.cartIdList(List.of(savedCart.getId()))
						.build();

					orderFacade.order(new SessionUser(savedUser), request, orderDateTime);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		//then
		List<User> users = userRepository.findAll();
		assertThat(users.get(0).getPoint()).isEqualTo(0);
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
			.email("test@coffeekiosk.com")
			.name("우경서")
			.role(Role.USER)
			.point(point)
			.build();
	}

	private Cart createCart(User user, Item item, int count) {
		return Cart.builder()
			.user(user)
			.item(item)
			.itemCount(count)
			.build();
	}

}