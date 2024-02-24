package com.coffeekiosk.coffeekiosk.service.item;

import static com.coffeekiosk.coffeekiosk.domain.item.ItemType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coffeekiosk.coffeekiosk.service.IntegrationTestSupport;
import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepository;
import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSaveServiceRequest;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemSearchServiceRequest;
import com.coffeekiosk.coffeekiosk.service.item.dto.request.ItemUpdateServiceRequest;
import com.coffeekiosk.coffeekiosk.service.item.dto.response.ItemResponse;

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
		Long itemId = itemService.createItem(request, lastModifiedDateTime);

		//then
		assertThat(itemId).isNotNull();

		List<Item> items = itemRepository.findAll();
		assertThat(items).hasSize(1)
			.extracting("id", "name", "itemType", "price", "lastModifiedDateTime")
			.containsExactlyInAnyOrder(
				tuple(itemId, "카페라떼", COFFEE, 5000, lastModifiedDateTime)
			);

	}

	@DisplayName("상품 정보를 수정한다.")
	@Test
	void updateItem() {
		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Item item = createItem("카페라떼", COFFEE, lastModifiedDateTime);
		Item savedItem = itemRepository.save(item);

		LocalDateTime updatedModifiedDateTime = LocalDateTime.of(2023, 11, 22, 0, 0);

		ItemUpdateServiceRequest request = ItemUpdateServiceRequest.builder()
			.name("케이크")
			.itemType(DESSERT)
			.price(6000)
			.build();

		//when
		itemService.updateItem(savedItem.getId(), request, updatedModifiedDateTime);

		//then
		List<Item> items = itemRepository.findAll();
		assertThat(items).hasSize(1)
			.extracting("id", "name", "itemType", "price", "lastModifiedDateTime")
			.containsExactlyInAnyOrder(
				tuple(savedItem.getId(), "케이크", DESSERT, 6000, updatedModifiedDateTime)
			);

	}

	@DisplayName("상품을 삭제한다.")
	@Test
	void deleteItem() {
		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Item item = createItem("카페라떼", COFFEE, lastModifiedDateTime);
		Item savedItem = itemRepository.save(item);

		//when
		itemService.deleteItem(savedItem.getId());

		//then
		List<Item> items = itemRepository.findAll();
		assertThat(items).hasSize(0)
			.isEmpty();

	}

	@DisplayName("상품 목록을 첫페이지를 조회한다.")
	@Test
	void findItemsFirstPage() {
		//given
		for (int i = 1; i <= 30; i++) {
			LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, i, 0, 0);
			itemRepository.save(createItem("카페라떼" + i, COFFEE, lastModifiedDateTime));
		}

		ItemSearchServiceRequest request = ItemSearchServiceRequest.builder()
			.build();

		//when
		List<ItemResponse> itemResponses = itemService.findItems(null, request, 10);

		//then
		assertThat(itemResponses).hasSize(10);
		assertThat(itemResponses.get(0).getName()).isEqualTo("카페라떼30");
		assertThat(itemResponses.get(9).getName()).isEqualTo("카페라떼21");
	}

	@DisplayName("상품 목록을 두번째 페이지를 조회한다.")
	@Test
	void findItemsSecondPage() {
		//given
		for (int i = 1; i <= 30; i++) {
			LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, i, 0, 0);
			itemRepository.save(createItem("카페라떼" + i, COFFEE, lastModifiedDateTime));
		}

		ItemSearchServiceRequest request = ItemSearchServiceRequest.builder()
			.build();

		List<Item> all = itemRepository.findAll();
		Item item = all.get(20);

		//when
		List<ItemResponse> itemResponses = itemService.findItems(item.getId(), request, 10);

		//then
		assertThat(itemResponses).hasSize(10);
		assertThat(itemResponses.get(0).getName()).isEqualTo(all.get(19).getName());
		assertThat(itemResponses.get(9).getName()).isEqualTo(all.get(10).getName());
	}

	@DisplayName("상품 목록을 이름으로 검색한다.")
	@Test
	void searchItemsByName() {
		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Item item1 = itemRepository.save(createItem("카페라떼", COFFEE, LocalDateTime.of(2023, 11, 21, 0, 0)));
		Item item2 = itemRepository.save(createItem("바닐라라떼", COFFEE, LocalDateTime.of(2023, 11, 22, 0, 0)));
		Item item3 = itemRepository.save(createItem("딸기케이크", DESSERT, LocalDateTime.of(2023, 11, 23, 0, 0)));

		ItemSearchServiceRequest request = ItemSearchServiceRequest.builder()
			.name("라떼")
			.build();

		//when
		List<ItemResponse> itemResponses = itemService.findItems(null, request, 10);

		//then
		assertThat(itemResponses)
			.hasSize(2)
			.extracting("name")
			.containsExactly(item2.getName(), item1.getName());
	}

	@DisplayName("상품 목록을 상품 타입별로 조회한다.")
	@Test
	void searchItemsByType() {
		//given
		Item item1 = itemRepository.save(createItem("카페라떼", COFFEE, LocalDateTime.of(2023, 11, 21, 0, 0)));
		Item item2 = itemRepository.save(createItem("바나나케이크", DESSERT, LocalDateTime.of(2023, 11, 22, 0, 0)));
		Item item3 = itemRepository.save(createItem("딸기케이크", DESSERT, LocalDateTime.of(2023, 11, 23, 0, 0)));

		ItemSearchServiceRequest request = ItemSearchServiceRequest.builder()
			.itemType(DESSERT)
			.build();

		//when
		List<ItemResponse> itemResponses = itemService.findItems(null, request, 10);

		//then
		assertThat(itemResponses)
			.hasSize(2)
			.extracting("name")
			.containsExactly(item3.getName(), item2.getName());
	}

	@DisplayName("상품 목록을 상품타입과 이름으로 조회한다.")
	@Test
	void searchItemsByNameAndType() {
		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Item item1 = itemRepository.save(createItem("카페라떼", COFFEE, lastModifiedDateTime));
		Item item2 = itemRepository.save(createItem("딸기라떼", COFFEE, lastModifiedDateTime));
		Item item3 = itemRepository.save(createItem("딸기케이크", DESSERT, lastModifiedDateTime));

		ItemSearchServiceRequest request = ItemSearchServiceRequest.builder()
			.name("딸기")
			.itemType(DESSERT)
			.build();

		//when
		List<ItemResponse> itemResponses = itemService.findItems(null, request, 10);

		//then
		assertThat(itemResponses)
			.hasSize(1)
			.extracting("name")
			.containsExactly(item3.getName());
	}

	@DisplayName("상품 상세정보를 조회한다.")
	@Test
	void findItemById() {
		//given
		LocalDateTime lastModifiedDateTime = LocalDateTime.of(2023, 11, 21, 0, 0);
		Item item = itemRepository.save(createItem("카페라떼", COFFEE, lastModifiedDateTime));

		//when
		ItemResponse itemResponse = itemService.findItem(item.getId());

		//then
		assertThat(itemResponse)
			.extracting("name", "itemType", "price", "lastModifiedDateTime")
			.contains(item.getName(), item.getItemType(), item.getPrice(), lastModifiedDateTime);
	}

	private Item createItem(String name, ItemType type, LocalDateTime lastModifiedDateTime) {
		return Item.builder()
			.name(name)
			.itemType(type)
			.price(5000)
			.lastModifiedDateTime(lastModifiedDateTime)
			.build();
	}
}