package com.coffeekiosk.coffeekiosk.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.coffeekiosk.coffeekiosk.ControllerTestSupport;
import com.coffeekiosk.coffeekiosk.controller.dto.request.ItemSaveRequest;
import com.coffeekiosk.coffeekiosk.service.ItemService;

@WebMvcTest(controllers = ItemApiController.class)
class ItemApiControllerTest extends ControllerTestSupport {

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

		//when //then
		mockMvc.perform(
				post("/api/items")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
	
	@DisplayName("상품 등록 시 상품 이름은 필수값입니다.")
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
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors.[0].value").value(""))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("상품 이름은 필수입니다."));
	}

	@DisplayName("상품 등록 시 상품 이름은 최소1글자 이상입니다.")
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
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("name"))
			.andExpect(jsonPath("$.fieldErrors.[0].value").value(""))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("상품 이름은 필수입니다."));

	}

	@DisplayName("상품 등록 시 상품 타입은 필수값입니다.")
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
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("itemType"))
			.andExpect(jsonPath("$.fieldErrors.[0].value").value(""))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("상품 타입은 필수입니다."));
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
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("itemType"))
			.andExpect(jsonPath("$.fieldErrors.[0].value").value(""))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("상품 타입은 필수입니다."));

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
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("유효하지 않는 상품 타입입니다."));
	}

	@DisplayName("상품 등록 시 상품 가격은 양수여야합니다.")
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
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("price"))
			.andExpect(jsonPath("$.fieldErrors.[0].value").value("0"))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("상품 가격은 양수여야 합니다."));
	}
}