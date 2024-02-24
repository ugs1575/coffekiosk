package com.coffeekiosk.coffeekiosk.service.cart;

import static com.coffeekiosk.coffeekiosk.domain.item.ItemType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

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
import com.coffeekiosk.coffeekiosk.domain.user.Role;
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

	@DisplayName("기존에 장바구니에 담긴 상품이면 수량만 더한다.")
	@Test
	void updateCartItemCount() {
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
		CartResponse cartResponse = cartService.updateCartItem(new SessionUser(savedUser), request);

		//then
		assertThat(cartResponse)
			.extracting("id", "itemId",  "itemName", "itemPrice", "itemCount")
			.contains(savedCart.getId(), savedItem.getId(), savedItem.getName(), savedItem.getPrice(), 3);
	}

	@DisplayName("장바구니에 20개 이상은 담을 수 없다.")
	@Test
	void updateCartItemMaxCount() {
		//given
		Item item1 = createItem();
		Item savedItem1 = itemRepository.save(item1);

		Item item2 = createItem();
		Item savedItem2 = itemRepository.save(item2);

		User user = createUser();
		User savedUser = userRepository.save(user);

		Cart cart1 = createCart(savedUser, savedItem1, 10);
		Cart savedCart1 = cartRepository.save(cart1);

		Cart cart2 = createCart(savedUser, savedItem2, 10);
		Cart savedCart2 = cartRepository.save(cart2);

		CartSaveServiceRequest request = CartSaveServiceRequest.builder()
			.itemId(savedItem1.getId())
			.count(1)
			.build();

		//when, then
		assertThatThrownBy(() -> cartService.updateCartItem(new SessionUser(savedUser), request))
			.isInstanceOf(BusinessException.class)
			.hasMessage("최대 주문 가능 수량은 20개 입니다.");
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
		CartResponse cartResponse = cartService.updateCartItem(new SessionUser(savedUser), request);

		//then
		assertThat(cartResponse.getId()).isNotNull();
		assertThat(cartResponse)
			.extracting("itemId",  "itemName", "itemPrice",  "itemCount")
			.contains(savedItem.getId(), savedItem.getName(),savedItem.getPrice(), 2);
	}

	@DisplayName("장바구니에서 선택한 아이템을 삭제한다.")
	@Test
	void deleteCartItem() {
		//given
		Item item = createItem();
		Item savedItem = itemRepository.save(item);

		User user = createUser();
		User savedUser = userRepository.save(user);

		Cart cart = createCart(savedUser, savedItem, 1);
		Cart savedCart = cartRepository.save(cart);

		//when
		cartService.deleteCartItem(savedCart.getId(), new SessionUser(savedUser));

		//then
		List<Cart> cartItems = cartRepository.findAll();
		assertThat(cartItems).hasSize(0)
			.isEmpty();
	}

	@DisplayName("장바구니에 담긴 상품목록을 조회한다.")
	@Test
	void findCartItems() {
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
		List<CartResponse> cartResponse = cartService.findCartItems(new SessionUser(savedUser));

		//then
		assertThat(cartResponse)
			.extracting("id", "itemId",  "itemName", "itemPrice", "itemCount")
			.contains(
				tuple(savedCart1.getId(), savedItem1.getId(), savedItem1.getName(), savedItem1.getPrice(), 1),
				tuple(savedCart2.getId(), savedItem2.getId(), savedItem2.getName(), savedItem2.getPrice(), 2)
			);
	}

	private Cart createCart(User user, Item item, int count) {
		return Cart.builder()
			.user(user)
			.item(item)
			.itemCount(count)
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
			.email("test@coffeekiosk.com")
			.name("우경서")
			.role(Role.USER)
			.build();
	}
}