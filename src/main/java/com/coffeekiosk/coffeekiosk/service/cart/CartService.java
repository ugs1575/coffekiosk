package com.coffeekiosk.coffeekiosk.service.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.cart.Cart;
import com.coffeekiosk.coffeekiosk.domain.cart.CartRepository;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.cart.dto.response.CartResponse;
import com.coffeekiosk.coffeekiosk.service.cart.dto.request.CartSaveServiceRequest;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartService {

	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final CartRepository cartRepository;

	@Transactional
	public CartResponse updateCartItem(Long userId, CartSaveServiceRequest request) {
		Item item = findItem(request.getItemId());
		User user = findUser(userId);

		Optional<Cart> cart = cartRepository.findByUserIdAndItemIdFetchJoin(userId, request.getItemId());

		if (cart.isPresent()) {
			cart.get().updateCount(request.getCount());
			return CartResponse.of(cart.get());
		}

		Long cartId = createCart(request, item, user);
		Cart createdCart = cartRepository.findByIdFetchJoin(cartId).get();
		return CartResponse.of(createdCart);
	}

	@Transactional
	public void deleteCartItem(Long id, Long userId) {
		cartRepository.deleteByIdAndUserId(id, userId);
	}

	public List<CartResponse> findCartItems(Long userId) {
		List<Cart> cartItems = cartRepository.findAllByUserIdFetchJoin(userId);
		return CartResponse.listOf(cartItems);
	}

	private User findUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}

	private Item findItem(Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}

	private Long createCart(CartSaveServiceRequest request, Item item, User user) {
		Cart cart = Cart.createCart(user, item, request.getCount());
		Cart savedCart = cartRepository.save(cart);
		return savedCart.getId();
	}

}
