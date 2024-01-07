package com.coffeekiosk.coffeekiosk.controller.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.coffeekiosk.coffeekiosk.ControllerTestSupport;
import com.coffeekiosk.coffeekiosk.controller.item.dto.request.ItemSaveRequest;
import com.coffeekiosk.coffeekiosk.controller.order.dto.request.OrderItemSaveRequest;
import com.coffeekiosk.coffeekiosk.controller.order.dto.request.OrderSaveRequest;
import com.coffeekiosk.coffeekiosk.service.order.OrderService;

@WebMvcTest(controllers = OrderApiController.class)
class OrderApiControllerTest extends ControllerTestSupport {

	@MockBean
	protected OrderService orderService;

	@DisplayName("상품을 주문한다.")
	@Test
	void createOrder() throws Exception {
		//given
		OrderItemSaveRequest itemRequest = OrderItemSaveRequest.builder()
			.itemId(1L)
			.count(1)
			.build();
		OrderSaveRequest request = OrderSaveRequest.builder()
			.orderList(List.of(itemRequest))
			.build();

		when(orderService.order(any(), any(), any())).thenReturn(1L);

		//when //then
		mockMvc.perform(
				post("/api/users/{userId}/orders", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data.id").value(1L));
	}

	@DisplayName("상품 주문시 주문 목록은 필수 값이다.")
	@Test
	void createOrderWithoutOrderList() throws Exception {
		//given
		OrderSaveRequest request = OrderSaveRequest.builder()
			.orderList(List.of())
			.build();

		//when //then
		mockMvc.perform(
				post("/api/users/{userId}/orders", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("orderList"))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("주문 목록은 필수입니다."));
	}

	@DisplayName("상품 주문시 상품 아이디는 필수 값이다.")
	@Test
	void createOrderWithoutItemId() throws Exception {
		//given
		OrderItemSaveRequest itemRequest = OrderItemSaveRequest.builder()
			.count(1)
			.build();
		OrderSaveRequest request = OrderSaveRequest.builder()
			.orderList(List.of(itemRequest))
			.build();

		//when //then
		mockMvc.perform(
				post("/api/users/{userId}/orders", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("orderList[0].itemId"))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("상품 아이디는 필수입니다."));
	}

	@DisplayName("상품 주문시 상품 아이디는 최소 1 이상이다.")
	@Test
	void createOrderWithInvalidItemId() throws Exception {
		//given
		OrderItemSaveRequest itemRequest = OrderItemSaveRequest.builder()
			.itemId(0L)
			.count(1)
			.build();
		OrderSaveRequest request = OrderSaveRequest.builder()
			.orderList(List.of(itemRequest))
			.build();

		//when //then
		mockMvc.perform(
				post("/api/users/{userId}/orders", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("orderList[0].itemId"))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("상품 아이디는 양수입니다."));
	}

	@DisplayName("상품 주문시 상품 개수는 최소 1개 이상이다.")
	@Test
	void createOrderWithInvalidCount() throws Exception {
		//given
		OrderItemSaveRequest itemRequest = OrderItemSaveRequest.builder()
			.itemId(1L)
			.count(0)
			.build();
		OrderSaveRequest request = OrderSaveRequest.builder()
			.orderList(List.of(itemRequest))
			.build();

		//when //then
		mockMvc.perform(
				post("/api/users/{userId}/orders", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("orderList[0].count"))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("최소 주문 상품 수는 1개 이상이어야 합니다."));
	}
}