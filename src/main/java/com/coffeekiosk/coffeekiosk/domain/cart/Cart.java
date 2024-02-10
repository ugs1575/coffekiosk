package com.coffeekiosk.coffeekiosk.domain.cart;

import static jakarta.persistence.FetchType.*;

import com.coffeekiosk.coffeekiosk.common.domain.BaseTimeEntity;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.user.User;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Cart extends BaseTimeEntity {
	private static final int MAX_ORDER_COUNT = 20;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(nullable = false)
	private int count;

	@Builder
	private Cart(User user, Item item, int count) {
		this.user = user;
		this.item = item;
		this.count = count;
	}

	public static Cart createCart(User user, Item item, int count) {
		return Cart.builder()
			.user(user)
			.item(item)
			.count(count)
			.build();
	}

	public void addCount(int count) {
		int totalCount = this.count + count;

		if (totalCount > MAX_ORDER_COUNT) {
			throw new BusinessException(ErrorCode.OVER_MAX_ORDER_COUNT);
		}

		this.count = totalCount;
	}
}
