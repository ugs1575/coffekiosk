package com.coffeekiosk.coffeekiosk.service.cart;

import static com.coffeekiosk.coffeekiosk.domain.item.ItemType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

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

	@DisplayName("기존에 카트에 담긴 상품이면 수량만 수정한다.")
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

	@DisplayName("기존에 카트에 담긴 상품이 아니면 새로 생성한다.")
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