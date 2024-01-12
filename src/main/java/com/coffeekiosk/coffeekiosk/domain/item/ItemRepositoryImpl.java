package com.coffeekiosk.coffeekiosk.domain.item;

import static com.coffeekiosk.coffeekiosk.domain.item.QItem.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSearchServiceRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public ItemRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Item> search(ItemSearchServiceRequest request, Pageable pageable) {
		List<Item> content = queryFactory
			.selectFrom(item)
			.where(
				nameEq(request.getName()),
				itemTypeEq(request.getItemType())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(item.count())
			.from(item)
			.where(
				nameEq(request.getName()),
				itemTypeEq(request.getItemType())
			);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private BooleanExpression nameEq(String keyword) {
		return hasText(keyword) ? item.name.contains(keyword) : null;
	}


	private BooleanExpression itemTypeEq(ItemType itemType) {
		return itemType != null ? item.itemType.eq(itemType) : null;
	}
}
