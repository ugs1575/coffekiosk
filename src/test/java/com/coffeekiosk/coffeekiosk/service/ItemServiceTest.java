package com.coffeekiosk.coffeekiosk.service;

import static com.coffeekiosk.coffeekiosk.domain.ItemType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.domain.Item;
import com.coffeekiosk.coffeekiosk.domain.ItemRepository;
import com.coffeekiosk.coffeekiosk.service.dto.request.ItemSaveServiceRequest;
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

	@DisplayName("상품 정보를 받아 저장한다.")
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

}