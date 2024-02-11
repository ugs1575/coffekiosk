package com.coffeekiosk.coffeekiosk.controller.order;

import static org.mockito.ArgumentMatchers.any;
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
import com.coffeekiosk.coffeekiosk.controller.order.api.OrderApiController;
import com.coffeekiosk.coffeekiosk.controller.order.api.dto.request.OrderSaveRequest;
import com.coffeekiosk.coffeekiosk.docs.order.OrderDocumentation;
import com.coffeekiosk.coffeekiosk.facade.RedissonLockOrderFacade;
import com.coffeekiosk.coffeekiosk.service.order.OrderHistoryService;
import com.coffeekiosk.coffeekiosk.service.order.dto.response.OrderItemResponse;
import com.coffeekiosk.coffeekiosk.service.order.dto.response.OrderResponse;

@WebMvcTest(controllers = OrderApiController.class)
class OrderApiControllerTest extends RestDocsSupport {

	@MockBean
	protected RedissonLockOrderFacade orderFacade;

	@MockBean
	protected OrderHistoryService orderHistoryService;

	@DisplayName("상품을 주문한다.")
	@Test
	void createOrder() throws Exception {
		//given
		OrderSaveRequest request = OrderSaveRequest.builder()
			.cartIdList(List.of(1L))
			.build();

		when(orderFacade.order(any(), any(), any())).thenReturn(1L);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/users/{userId}/orders", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("201"))
			.andExpect(jsonPath("$.message").value("CREATED"))
			.andExpect(header().string("Location", "/api/users/1/orders/1"))
			.andDo(OrderDocumentation.createOrder());
	}

	@DisplayName("상품 주문시 주문 목록은 필수 값이다.")
	@Test
	void createOrderWithoutOrderList() throws Exception {
		//given
		OrderSaveRequest request = OrderSaveRequest.builder()
			.cartIdList(List.of())
			.build();

		//when //then
		mockMvc.perform(
				post("/api/users/{userId}/orders", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("cartIdList"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("주문 목록은 필수입니다."));
	}
	
	@DisplayName("주문 상세내역을 조회한다.")
	@Test
	void findOrder() throws Exception {
		//given
		OrderItemResponse orderItemResponse = OrderItemResponse.builder()
			.itemId(1L)
			.itemName("아이스아메리카노")
			.itemPrice(1000)
			.orderCount(10)
			.orderPrice(10000)
			.build();

		OrderResponse response = OrderResponse.builder()
			.id(1L)
			.totalPrice(10000)
			.orderDateTime(LocalDateTime.now())
			.orderItems(List.of(orderItemResponse))
			.build();

		when(orderHistoryService.findOrder(any(), any())).thenReturn(response);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/users/{userId}/orders/{orderId}", 1L, 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(OrderDocumentation.findOrder());
	    
	}

	@DisplayName("상품 목록 조회을 조회할 수 있다.")
	@Test
	void findOrders() throws Exception {
		//given
		OrderItemResponse orderItemResponse = OrderItemResponse.builder()
			.itemId(1L)
			.itemName("아이스아메리카노")
			.itemPrice(1000)
			.orderCount(10)
			.orderPrice(10000)
			.build();

		OrderResponse response = OrderResponse.builder()
			.id(1L)
			.totalPrice(10000)
			.orderDateTime(LocalDateTime.now())
			.orderItems(List.of(orderItemResponse))
			.build();

		List<OrderResponse> result = List.of(response);

		when(orderHistoryService.findOrders(any(), any())).thenReturn(result);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/users/{userId}/orders", 1L)
					.queryParam("endDate", "2023-11-21T00:00:00")
					.queryParam("startDate", "2023-11-22T00:00:00")
					.queryParam("page", "0")
					.queryParam("size", "1")
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray())
			.andDo(OrderDocumentation.findOrders());
	}
}