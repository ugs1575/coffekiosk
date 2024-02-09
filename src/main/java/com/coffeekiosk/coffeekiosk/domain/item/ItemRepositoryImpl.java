package com.coffeekiosk.coffeekiosk.domain.item;

import static com.coffeekiosk.coffeekiosk.domain.item.QItem.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;

import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSearchServiceRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public ItemRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<Item> search(Long itemId, ItemSearchServiceRequest request, int pageSize) {
		return queryFactory
			.selectFrom(item)
			.where(
				ltItemId(itemId),
				nameEq(request.getName()),
				itemTypeEq(request.getItemType())
			)
			.orderBy(item.id.desc())
			.limit(pageSize)
			.fetch();
	}

	private BooleanExpression ltItemId(Long itemId) {
		if (itemId == null) {
			return null;
		}

		return item.id.lt(itemId);
	}

	private BooleanExpression nameEq(String keyword) {
		return hasText(keyword) ? item.name.contains(keyword) : null;
	}


	private BooleanExpression itemTypeEq(ItemType itemType) {
		return itemType != null ? item.itemType.eq(itemType) : null;
	}
}
