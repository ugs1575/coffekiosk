package com.coffeekiosk.coffeekiosk.docs.order;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class OrderDocumentation {
	public static RestDocumentationResultHandler createOrder() {
		return document("order/create",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("userId").description("사용자 ID")
			),
			requestFields(
				fieldWithPath("cartIdList").type(JsonFieldType.ARRAY).description("주문 목록")
			),
			responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER)
					.description("코드"),
				fieldWithPath("message").type(JsonFieldType.STRING)
					.description("메시지"),
				fieldWithPath("data").type(JsonFieldType.NULL)
					.description("응답 데이터")
			),
			responseHeaders(
				headerWithName("Location").description("생성된 주문 ID")
			)
		);
	}

	public static RestDocumentationResultHandler findOrder() {
		return document("order/find",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("userId").description("사용자 ID"),
				parameterWithName("orderId").description("주문 ID")
			),
			responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER)
					.description("코드"),
				fieldWithPath("message").type(JsonFieldType.STRING)
					.description("메시지"),
				fieldWithPath("data").type(JsonFieldType.OBJECT)
					.description("응답 데이터"),
				fieldWithPath("data.id").type(JsonFieldType.NUMBER)
					.description("주문 ID"),
				fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER)
					.description("주문 금액"),
				fieldWithPath("data.orderDateTime").type(JsonFieldType.STRING)
					.description("주문 시각"),
				fieldWithPath("data.orderItems").type(JsonFieldType.ARRAY)
					.description("주문 상품 목록"),
				fieldWithPath("data.orderItems.[].itemId").type(JsonFieldType.NUMBER)
					.description("주문 상품 ID"),
				fieldWithPath("data.orderItems.[].itemName").type(JsonFieldType.STRING)
					.description("주문 상품명"),
				fieldWithPath("data.orderItems.[].itemPrice").type(JsonFieldType.NUMBER)
					.description("상품 개별 금액"),
				fieldWithPath("data.orderItems.[].count").type(JsonFieldType.NUMBER)
					.description("상품 주문 수량"),
				fieldWithPath("data.orderItems.[].orderPrice").type(JsonFieldType.NUMBER)
					.description("상품별 주문 금액")
			));
	}

	public static RestDocumentationResultHandler findOrders() {
		return document("order/findAll",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("userId").description("사용자 ID")
			),
			queryParameters(
				parameterWithName("page").description("페이지 번호"),
				parameterWithName("size").description("한 페이지 당 데이터 수"),
				parameterWithName("startDate").description("주문 내역 검색 시작일 (yyyy-MM-dd'T'HH:mm:ss)"),
				parameterWithName("endDate").description("주문 내역 검색 종료일 (yyyy-MM-dd'T'HH:mm:ss)")
			),
			responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER)
					.description("코드"),
				fieldWithPath("message").type(JsonFieldType.STRING)
					.description("메시지"),
				fieldWithPath("data").type(JsonFieldType.ARRAY)
					.description("응답 데이터"),
				fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
					.description("주문 ID"),
				fieldWithPath("data.[].totalPrice").type(JsonFieldType.NUMBER)
					.description("주문 금액"),
				fieldWithPath("data.[].orderDateTime").type(JsonFieldType.STRING)
					.description("주문 시각"),
				fieldWithPath("data.[].orderItems").type(JsonFieldType.ARRAY)
					.description("주문 상품 목록"),
				fieldWithPath("data.[].orderItems.[].itemId").type(JsonFieldType.NUMBER)
					.description("주문 상품 ID"),
				fieldWithPath("data.[].orderItems.[].itemName").type(JsonFieldType.STRING)
					.description("주문 상품명"),
				fieldWithPath("data.[].orderItems.[].itemPrice").type(JsonFieldType.NUMBER)
					.description("상품 개별 금액"),
				fieldWithPath("data.[].orderItems.[].count").type(JsonFieldType.NUMBER)
					.description("상품 주문 수량"),
				fieldWithPath("data.[].orderItems.[].orderPrice").type(JsonFieldType.NUMBER)
					.description("상품별 주문 금액")
			));
	}
}
