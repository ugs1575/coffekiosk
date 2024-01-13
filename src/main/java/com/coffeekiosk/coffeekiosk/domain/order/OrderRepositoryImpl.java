package com.coffeekiosk.coffeekiosk.domain.order;

import static com.coffeekiosk.coffeekiosk.domain.item.QItem.*;
import static com.coffeekiosk.coffeekiosk.domain.order.QOrder.*;
import static com.coffeekiosk.coffeekiosk.domain.user.QUser.*;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.coffeekiosk.coffeekiosk.domain.user.QUser;
import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSearchServiceRequest;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public OrderRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<Order> findOrders(Long userId, OrderSearchServiceRequest request, Pageable pageable) {
		return queryFactory
			.selectFrom(order)
			.join(order.user, user)
			.where(
				userIdEq(userId),
				order.orderDateTime.between(request.getStartDate(), request.getEndDate())
			)
			.orderBy(order.orderDateTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private BooleanExpression userIdEq(Long userId) {
		return userId == null ? null : user.id.eq(userId);
	}
}
