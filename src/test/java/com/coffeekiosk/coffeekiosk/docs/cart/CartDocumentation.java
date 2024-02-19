package com.coffeekiosk.coffeekiosk.docs.cart;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class CartDocumentation {
	public static RestDocumentationResultHandler updateCart() {
		return document("cart/update",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			requestFields(
				fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품 ID"),
				fieldWithPath("count").type(JsonFieldType.NUMBER).description("상품 수량")
			),
			responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER)
					.description("코드"),
				fieldWithPath("message").type(JsonFieldType.STRING)
					.description("메시지"),
				fieldWithPath("data.id").type(JsonFieldType.NUMBER)
					.description("장바구니 ID"),
				fieldWithPath("data.itemId").type(JsonFieldType.NUMBER)
					.description("상품 ID"),
				fieldWithPath("data.itemName").type(JsonFieldType.STRING)
					.description("상품명"),
				fieldWithPath("data.itemPrice").type(JsonFieldType.NUMBER)
					.description("상품가격"),
				fieldWithPath("data.itemCount").type(JsonFieldType.NUMBER)
					.description("상품수량")
			)
		);
	}

	public static RestDocumentationResultHandler deleteCart() {
		return document("cart/delete",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("cartId").description("장바구니 ID")
			),
			responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER)
					.description("코드"),
				fieldWithPath("message").type(JsonFieldType.STRING)
					.description("메시지"),
				fieldWithPath("data").type(JsonFieldType.NULL)
					.description("응답 데이터")
			));
	}

	public static RestDocumentationResultHandler findCarts() {
		return document("cart/findAll",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER)
					.description("코드"),
				fieldWithPath("message").type(JsonFieldType.STRING)
					.description("메시지"),
				fieldWithPath("data").type(JsonFieldType.ARRAY)
					.description("응답 데이터"),
				fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
					.description("장바구니 ID"),
				fieldWithPath("data.[].itemId").type(JsonFieldType.NUMBER)
					.description("상품 ID"),
				fieldWithPath("data.[].itemName").type(JsonFieldType.STRING)
					.description("상품명"),
				fieldWithPath("data.[].itemPrice").type(JsonFieldType.NUMBER)
					.description("상품가격"),
				fieldWithPath("data.[].itemCount").type(JsonFieldType.NUMBER)
					.description("상품수량")
			));
	}
}
