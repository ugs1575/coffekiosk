package com.coffeekiosk.coffeekiosk.service.cart;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.config.auth.dto.SessionUser;
import com.coffeekiosk.coffeekiosk.domain.cart.Cart;
import com.coffeekiosk.coffeekiosk.domain.cart.CartRepository;
import com.coffeekiosk.coffeekiosk.domain.cart.Carts;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.domain.user.UserRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.cart.dto.request.CartSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.cart.dto.response.CartResponse;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartService {
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final CartRepository cartRepository;

	@Transactional
	public CartResponse updateCartItem(SessionUser sessionUser, CartSaveServiceRequest request) {
		Item item = findItem(request.getItemId());
		User user = findUser(sessionUser.getId());

		List<Cart> cartItems = cartRepository.findByUserIdFetchJoin(user.getId());
		Carts carts = new Carts(cartItems);

		if (carts.isOverMaxOrderCount(request.getCount())) {
			throw new BusinessException(ErrorCode.OVER_MAX_ORDER_COUNT);
		}

		if (carts.containsItem(item.getId())) {
			Cart cart = cartRepository.findByUserAndItem(user, item);
			cart.addCount(request.getCount());
			return CartResponse.of(cart);
		}

		Cart savedCart = cartRepository.save(Cart.createCart(user, item, request.getCount()));
		return CartResponse.of(savedCart);
	}

	@Transactional
	public void deleteCartItem(Long id, SessionUser sessionUser) {
		cartRepository.deleteByIdAndUserId(id, sessionUser.getId());
	}

	public List<CartResponse> findCartItems(SessionUser sessionUser) {
		List<Cart> cartItems = cartRepository.findByUserIdFetchJoin(sessionUser.getId());
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

}
