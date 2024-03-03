package com.coffeekiosk.coffeekiosk.domain.order;

import static com.coffeekiosk.coffeekiosk.domain.order.QOrder.*;

import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.coffeekiosk.coffeekiosk.service.order.dto.request.OrderSearchServiceRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public OrderRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<Order> findOrders(Long userId, OrderSearchServiceRequest request, Pageable pageable) {
		return queryFactory
			.selectFrom(order)
			.where(
				userIdEq(userId),
				order.orderDateTime.between(request.getStartDate(), request.getEndDate())
			)
			.orderBy(order.orderDateTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public Page<Order> findPageOrders(Long userId, OrderSearchServiceRequest request, Pageable pageable) {
		List<Order> content = queryFactory
			.selectFrom(order)
			.where(
				userIdEq(userId),
				order.orderDateTime.between(request.getStartDate(), request.getEndDate())
			)
			.orderBy(
				order.orderDateTime.desc(),
				order.id.desc()
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(order.count())
			.from(order)
			.where(
				userIdEq(userId),
				order.orderDateTime.between(request.getStartDate(), request.getEndDate())
			);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private BooleanExpression userIdEq(Long userId) {
		return userId == null ? null : order.user.id.eq(userId);
	}
}
