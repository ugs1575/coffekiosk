package com.coffeekiosk.coffeekiosk.controller.item;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.coffeekiosk.coffeekiosk.RestDocsSupport;
import com.coffeekiosk.coffeekiosk.controller.item.dto.request.ItemSaveRequest;
import com.coffeekiosk.coffeekiosk.controller.item.dto.request.ItemUpdateRequest;
import com.coffeekiosk.coffeekiosk.docs.item.ItemDocumentation;
import com.coffeekiosk.coffeekiosk.domain.item.ItemType;
import com.coffeekiosk.coffeekiosk.service.item.ItemService;
import com.coffeekiosk.coffeekiosk.service.item.dto.response.ItemResponse;

@WebMvcTest(controllers = ItemApiController.class)
class ItemApiControllerTest extends RestDocsSupport {

	@MockBean
	protected ItemService itemService;

	@DisplayName("상품을 등록한다")
	@Test
	void createItem() throws Exception {
		//given
		ItemSaveRequest request = ItemSaveRequest.builder()
			.name("카페라떼")
			.itemType("COFFEE")
			.price(5000)
			.build();

		when(itemService.createItem(any(), any())).thenReturn(1L);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/items")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("201"))
			.andExpect(jsonPath("$.message").value("CREATED"))
			.andExpect(header().string("Location", "/api/items/1"))
			.andDo(print())
			.andDo(ItemDocumentation.createItem());
	}

	@DisplayName("상품 등록 시 상품 이름은 필수값이다.")
	@Test
	void createItemWithoutName() throws Exception {
		//given
		ItemSaveRequest request = ItemSaveRequest.builder()
			.itemType("COFFEE")
			.price(5000)
			.build();

		//when //then
		mockMvc.perform(
				post("/api/items")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("상품 이름은 필수입니다."))
			.andDo(print());
	}

	@DisplayName("상품 등록 시 상품 이름은 최소1글자 이상이다.")
	@Test
	void createItemWithEmptyName() throws Exception {
		ItemSaveRequest request = ItemSaveRequest.builder()
			.name("")
			.itemType("COFFEE")
			.price(5000)
			.build();

		//when //then
		mockMvc.perform(
				post("/api/items")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("상품 이름은 필수입니다."))
			.andDo(print());

	}

	@DisplayName("상품 등록 시 상품 타입은 필수값이다.")
	@Test
	void createItemWithoutItemType() throws Exception {
		//given
		ItemSaveRequest request = ItemSaveRequest.builder()
			.name("카페라떼")
			.price(5000)
			.build();

		//when //then
		mockMvc.perform(
				post("/api/items")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("itemType"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("상품 타입은 필수입니다."))
			.andDo(print());
	}

	@DisplayName("상품 등록 시 상품 타입은 최소1글자 이상입니다.")
	@Test
	void createItemWithEmptyType() throws Exception {
		ItemSaveRequest request = ItemSaveRequest.builder()
			.name("카페라떼")
			.itemType("")
			.price(5000)
			.build();

		//when //then
		mockMvc.perform(
				post("/api/items")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("itemType"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("상품 타입은 필수입니다."))
			.andDo(print());

	}

	@DisplayName("상품 등록 시 상품 타입은 유효한 타입이어야합니다.")
	@Test
	void createItemWithInvalidType() throws Exception {
		//given
		ItemSaveRequest request = ItemSaveRequest.builder()
			.name("카페라떼")
			.price(5000)
			.itemType("coffee2")
			.build();

		//when //then
		mockMvc.perform(
				post("/api/items")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("유효하지 않는 상품 타입입니다."))
			.andDo(print());
	}

	@DisplayName("상품 등록 시 상품 가격은 최소 1원입니다.")
	@Test
	void createItemWithoutPrice() throws Exception {
		//given
		ItemSaveRequest request = ItemSaveRequest.builder()
			.name("카페라떼")
			.itemType("COFFEE")
			.price(0)
			.build();

		//when //then
		mockMvc.perform(
				post("/api/items")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("price"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("최소 상품 가격은 1원입니다."))
			.andDo(print());
	}

	@DisplayName("상품을 수정한다")
	@Test
	void updateItem() throws Exception {
		//given
		ItemUpdateRequest request = ItemUpdateRequest.builder()
			.name("카페라떼")
			.itemType("COFFEE")
			.price(5000)
			.build();

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.put("/api/items/{itemId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(print())
			.andDo(ItemDocumentation.updateItem());
	}

	@DisplayName("상품 수정 시 상품 이름은 필수값이다.")
	@Test
	void updateItemWithoutName() throws Exception {
		//given
		ItemUpdateRequest request = ItemUpdateRequest.builder()
			.itemType("COFFEE")
			.price(5000)
			.build();

		//when //then
		mockMvc.perform(
				put("/api/items/{itemId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("상품 이름은 필수입니다."))
			.andDo(print());
	}

	@DisplayName("상품 등록 시 상품 이름은 최소1글자 이상이다.")
	@Test
	void updateItemWithEmptyName() throws Exception {
		ItemUpdateRequest request = ItemUpdateRequest.builder()
			.name("")
			.itemType("COFFEE")
			.price(5000)
			.build();

		//when //then
		mockMvc.perform(
				put("/api/items/{itemId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("상품 이름은 필수입니다."))
			.andDo(print());

	}

	@DisplayName("상품 수정 시 상품 타입은 필수값이다.")
	@Test
	void updateItemWithoutItemType() throws Exception {
		//given
		ItemUpdateRequest request = ItemUpdateRequest.builder()
			.name("카페라떼")
			.price(5000)
			.build();

		//when //then
		mockMvc.perform(
				put("/api/items/{itemId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("itemType"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("상품 타입은 필수입니다."))
			.andDo(print());
	}

	@DisplayName("상품 수정 시 상품 타입은 최소1글자 이상입니다.")
	@Test
	void updateItemWithEmptyType() throws Exception {
		ItemUpdateRequest request = ItemUpdateRequest.builder()
			.name("카페라떼")
			.itemType("")
			.price(5000)
			.build();

		//when //then
		mockMvc.perform(
				put("/api/items/{itemId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("itemType"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("상품 타입은 필수입니다."))
			.andDo(print());

	}

	@DisplayName("상품 수정 시 상품 타입은 유효한 타입이어야합니다.")
	@Test
	void updateItemWithInvalidType() throws Exception {
		//given
		ItemUpdateRequest request = ItemUpdateRequest.builder()
			.name("카페라떼")
			.price(5000)
			.itemType("coffee2")
			.build();

		//when //then
		mockMvc.perform(
				put("/api/items/{itemId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("유효하지 않는 상품 타입입니다."))
			.andDo(print());
	}

	@DisplayName("상품 수정 시 상품 가격은 양수여야합니다.")
	@Test
	void updateItemWithoutPrice() throws Exception {
		//given
		ItemUpdateRequest request = ItemUpdateRequest.builder()
			.name("카페라떼")
			.itemType("COFFEE")
			.price(0)
			.build();

		//when //then
		mockMvc.perform(
				put("/api/items/{itemId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("price"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("상품 가격은 양수여야 합니다."))
			.andDo(print());
	}

	@DisplayName("상품을 삭제한다.")
	@Test
	void deleteItem() throws Exception {
		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/items/{itemId}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(print())
			.andDo(ItemDocumentation.deleteItem());
	}

	@DisplayName("상품 목록을 조회한다.")
	@Test
	void findItems() throws Exception {
		//given
		ItemResponse itemResponse = ItemResponse.builder()
			.id(1L)
			.name("아이스아메리카노")
			.itemType(ItemType.COFFEE)
			.price(1000)
			.lastModifiedDateTime(LocalDateTime.now())
			.build();

		List<ItemResponse> result = List.of(itemResponse);

		when(itemService.findItems(any(), any())).thenReturn(result);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/items")
					.contentType(MediaType.APPLICATION_JSON)
					.queryParam("page", "0")
					.queryParam("size", "1")
					.queryParam("name", "아이스")
					.queryParam("itemType", "COFFEE")
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray())
			.andDo(print())
			.andDo(ItemDocumentation.findItems());
	}

	@DisplayName("상품 목록 검색 시 검색 조건은 빈값으로 검색할 수 있다.")
	@Test
	void findItemsWithEmptySearchConditions() throws Exception {
		//given
		List<ItemResponse> result = List.of();

		when(itemService.findItems(any(), any())).thenReturn(result);

		//when //then
		mockMvc.perform(
				get("/api/items")
					.queryParam("name", "")
					.queryParam("itemType", "")
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray())
			.andDo(print());
	}

	@DisplayName("상품 타입으로 목록 검색 시 유효하지 않은 타입으로 검색할 수 없다.")
	@Test
	void findItemsWithEmptyType() throws Exception {
		//when //then
		mockMvc.perform(
				get("/api/items")
					.queryParam("itemType", "test")
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("유효하지 않는 상품 타입입니다."))
			.andDo(print());
	}

	@DisplayName("상품 상세정보를 조회한다.")
	@Test
	void findItem() throws Exception {
		//given
		ItemResponse itemResponse = ItemResponse.builder()
			.id(1L)
			.name("아이스아메리카노")
			.itemType(ItemType.COFFEE)
			.price(1000)
			.lastModifiedDateTime(LocalDateTime.now())
			.build();

		when(itemService.findItem(any())).thenReturn(itemResponse);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/items/{itemId}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(print())
			.andDo(ItemDocumentation.findItem());
	}


}