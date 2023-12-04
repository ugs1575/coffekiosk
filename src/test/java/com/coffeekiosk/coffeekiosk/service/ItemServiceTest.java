package com.coffeekiosk.coffeekiosk.service;

import static com.coffeekiosk.coffeekiosk.domain.ItemType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.coffeekiosk.coffeekiosk.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.domain.Item;
import com.coffeekiosk.coffeekiosk.domain.ItemRepository;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;
import com.coffeekiosk.coffeekiosk.service.dto.request.ItemSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.dto.request.ItemUpdateServiceRequest;
import com.coffeekiosk.coffeekiosk.service.dto.response.ItemResponse;

class ItemServiceTest extends IntegrationTestSupport {

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemRepository itemRepository;

	@AfterEach
	void tearDown() {
		itemRepository.deleteAllInBatch();
	}

	@DisplayName("상품 정보를 저장한다.")
	@Test
	void saveItem() {
		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);

		ItemSaveServiceRequest request = ItemSaveServiceRequest.builder()
			.name("카페라떼")
			.itemType(COFFEE)
			.price(5000)
			.build();

		//when
		ItemResponse itemResponse = itemService.createItem(request, lastModifiedDateTime);

		//then
		assertThat(itemResponse.getId()).isNotNull();
		assertThat(itemResponse)
			.extracting("name", "itemType", "price", "lastModifiedDateTime")
			.contains("카페라떼", "커피", 5000, lastModifiedDateTime);

		List<Item> items = itemRepository.findAll();
		assertThat(items).hasSize(1)
			.extracting("name", "itemType", "price", "lastModifiedDateTime")
			.containsExactlyInAnyOrder(
				tuple("카페라떼", COFFEE, 5000, lastModifiedDateTime)
			);

	}

	@DisplayName("상품 정보를 수정한다.")
	@Test
	void updateItem() {
		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Item item = Item.builder()
			.name("카페라떼")
			.itemType(COFFEE)
			.price(5000)
			.lastModifiedDateTime(lastModifiedDateTime)
			.build();
		Item savedItem = itemRepository.save(item);

		LocalDateTime updatedModifiedDateTime = LocalDateTime.of(2023, 11, 22, 0, 0);
		ItemUpdateServiceRequest request = ItemUpdateServiceRequest.builder()
			.name("케이크")
			.itemType(DESSERT)
			.price(6000)
			.build();

		//when
		ItemResponse itemResponse = itemService.updateItem(savedItem.getId(), request, updatedModifiedDateTime);

		//then
		assertThat(itemResponse.getId()).isEqualTo(savedItem.getId());
		assertThat(itemResponse)
			.extracting("name", "itemType", "price", "lastModifiedDateTime")
			.contains("케이크", "디저트", 6000, updatedModifiedDateTime);

		List<Item> items = itemRepository.findAll();
		assertThat(items).hasSize(1)
			.extracting("name", "itemType", "price", "lastModifiedDateTime")
			.containsExactlyInAnyOrder(
				tuple("케이크", DESSERT, 6000, updatedModifiedDateTime)
			);

	}

	@DisplayName("상품을 삭제한다.")
	@Test
	void deleteItem() {
		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Item item = Item.builder()
			.name("카페라떼")
			.itemType(COFFEE)
			.price(5000)
			.lastModifiedDateTime(lastModifiedDateTime)
			.build();
		Item savedItem = itemRepository.save(item);

		//when
		itemService.deleteItem(savedItem.getId());

		//then
		List<Item> items = itemRepository.findAll();
		assertThat(items).hasSize(0)
			.isEmpty();

	}

	@DisplayName("상품 목록을 조회한다.")
	@Test
	void findItems() {
	    //given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Item item1 = itemRepository.save(
			Item.builder()
			.name("카페라떼")
			.itemType(COFFEE)
			.price(5000)
			.lastModifiedDateTime(lastModifiedDateTime)
			.build()
		);

		Item item2 = itemRepository.save(
			Item.builder()
				.name("케이크")
				.itemType(DESSERT)
				.price(6000)
				.lastModifiedDateTime(lastModifiedDateTime)
				.build()
		);

		itemRepository.saveAll(List.of(item1, item2));

		PageRequest pageRequest = PageRequest.of(0, 3);

	    //when
		List<ItemResponse> items = itemService.findItems(pageRequest);

		//then
		assertThat(items)
			.extracting("name")
			.containsExactly(item1.getName(), item2.getName());

	}
}