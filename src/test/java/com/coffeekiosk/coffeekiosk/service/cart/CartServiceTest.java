package com.coffeekiosk.coffeekiosk.service.cart;

import static com.coffeekiosk.coffeekiosk.domain.item.ItemType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.domain.cart.Cart;
import com.coffeekiosk.coffeekiosk.domain.cart.CartRepository;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.service.cart.dto.response.CartResponse;
import com.coffeekiosk.coffeekiosk.service.cart.dto.request.CartSaveServiceRequest;

class CartServiceTest extends IntegrationTestSupport {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartService cartService;

	@AfterEach
	void tearDown() {
		cartRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
		itemRepository.deleteAllInBatch();
	}

	@DisplayName("기존에 장바구니에 담긴 상품이면 수량만 수정한다.")
	@Test
	void updateCartCount() {
		//given
		Item item = createItem();
		Item savedItem = itemRepository.save(item);

		User user = createUser();
		User savedUser = userRepository.save(user);

		Cart cart = createCart(savedUser, savedItem, 1);
		Cart savedCart = cartRepository.save(cart);

		CartSaveServiceRequest request = CartSaveServiceRequest.builder()
			.itemId(savedItem.getId())
			.count(2)
			.build();

		//when
		CartResponse cartResponse = cartService.updateCart(savedUser.getId(), request);

		//then
		assertThat(cartResponse)
			.extracting("id", "itemId",  "itemName", "count")
			.contains(savedCart.getId(), savedItem.getId(), savedItem.getName(), 2);
	}

	@DisplayName("기존에 장바구니에 담긴 상품이 아니면 새로 생성한다.")
	@Test
	void createCart() {
		//given
		Item item = createItem();
		Item savedItem = itemRepository.save(item);

		User user = createUser();
		User savedUser = userRepository.save(user);

		CartSaveServiceRequest request = CartSaveServiceRequest.builder()
			.itemId(savedItem.getId())
			.count(2)
			.build();

		//when
		CartResponse cartResponse = cartService.updateCart(savedUser.getId(), request);

		//then
		assertThat(cartResponse.getId()).isNotNull();
		assertThat(cartResponse)
			.extracting("itemId",  "itemName", "count")
			.contains(savedItem.getId(), savedItem.getName(), 2);
	}

	@DisplayName("장바구니에서 선택한 아이템을 삭제한다.")
	@Test
	void deleteCart() {
		//given
		Item item = createItem();
		Item savedItem = itemRepository.save(item);

		User user = createUser();
		User savedUser = userRepository.save(user);

		Cart cart = createCart(savedUser, savedItem, 1);
		Cart savedCart = cartRepository.save(cart);

		//when
		cartService.deleteCart(savedCart.getId(), savedUser.getId());

		//then
		List<Cart> carts = cartRepository.findAll();
		assertThat(carts).hasSize(0)
			.isEmpty();
	}

	@DisplayName("장바구니에 담긴 상품목록을 조회한다.")
	@Test
	void findCarts() {
		//given
		Item item1 = createItem();
		Item savedItem1 = itemRepository.save(item1);

		Item item2 = createItem();
		Item savedItem2 = itemRepository.save(item2);

		User user = createUser();
		User savedUser = userRepository.save(user);

		Cart cart1 = createCart(savedUser, savedItem1, 1);
		Cart savedCart1 = cartRepository.save(cart1);

		Cart cart2 = createCart(savedUser, savedItem2, 2);
		Cart savedCart2 = cartRepository.save(cart2);

		//when
		List<CartResponse> cartResponse = cartService.findCarts(savedUser.getId());

		//then
		assertThat(cartResponse)
			.extracting("id", "itemId",  "itemName", "count")
			.contains(
				tuple(savedCart1.getId(), savedItem1.getId(), savedItem1.getName(), 1),
				tuple(savedCart2.getId(), savedItem2.getId(), savedItem2.getName(), 2)
			);
	}

	private Cart createCart(User user, Item item, int count) {
		return Cart.builder()
			.user(user)
			.item(item)
			.count(count)
			.build();
	}

	private Item createItem() {
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		return Item.builder()
			.name("카페라떼")
			.itemType(COFFEE)
			.price(5000)
			.lastModifiedDateTime(lastModifiedDateTime)
			.build();
	}

	private User createUser() {
		return User.builder()
			.name("우경서")
			.build();
	}
}