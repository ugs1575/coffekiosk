package com.coffeekiosk.coffeekiosk.controller.cart;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.coffeekiosk.coffeekiosk.RestDocsSupport;
import com.coffeekiosk.coffeekiosk.controller.cart.api.CartApiController;
import com.coffeekiosk.coffeekiosk.controller.cart.api.dto.request.CartSaveRequest;
import com.coffeekiosk.coffeekiosk.docs.cart.CartDocumentation;
import com.coffeekiosk.coffeekiosk.service.cart.CartService;
import com.coffeekiosk.coffeekiosk.service.cart.dto.response.CartResponse;

@WebMvcTest(controllers = CartApiController.class)
class CartApiControllerTest extends RestDocsSupport {

	@MockBean
	protected CartService cartService;

	@DisplayName("장바구니에 상품을 담는다")
	@Test
	void updateCartItem() throws Exception {
	    //given
		CartSaveRequest request = CartSaveRequest.builder()
			.itemId(1L)
			.count(1)
			.build();

		CartResponse response = CartResponse.builder()
			.id(1L)
			.itemId(1L)
			.itemName("아이스아메리카노")
			.count(1)
			.build();

		when(cartService.updateCartItem(any(), any())).thenReturn(response);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/users/{userId}/carts", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(CartDocumentation.updateCart());

	}

	@DisplayName("장바구니에 담긴 상품을 삭제한다.")
	@Test
	void deleteCartItem() throws Exception {
		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/users/{userId}/carts/{cartId}", 1L, 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(CartDocumentation.deleteCart());

	}

	@DisplayName("장바구니에 담긴 상품 목록을 조회한다.")
	@Test
	void findCartItems() throws Exception {
		//given
		CartResponse response = CartResponse.builder()
			.id(1L)
			.itemId(1L)
			.itemName("아이스아메리카노")
			.count(1)
			.build();

		when(cartService.findCartItems(any())).thenReturn(List.of(response));

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/users/{userId}/carts", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray())
			.andDo(CartDocumentation.findCarts());

	}
}