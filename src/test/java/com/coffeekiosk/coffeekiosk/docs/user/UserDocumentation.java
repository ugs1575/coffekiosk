package com.coffeekiosk.coffeekiosk.docs.user;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class UserDocumentation {
	public static RestDocumentationResultHandler findUser() {
		return document("user/find",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			responseFields(
				fieldWithPath("code").type(JsonFieldType.STRING)
					.description("코드"),
				fieldWithPath("message").type(JsonFieldType.STRING)
					.description("메시지"),
				fieldWithPath("data").type(JsonFieldType.OBJECT)
					.description("응답 데이터"),
				fieldWithPath("data.id").type(JsonFieldType.NUMBER)
					.description("사용자 ID"),
				fieldWithPath("data.name").type(JsonFieldType.STRING)
					.description("사용자명"),
				fieldWithPath("data.point").type(JsonFieldType.NUMBER)
					.description("보유 포인트"),
				fieldWithPath("data.role").type(JsonFieldType.STRING)
					.description("사용자 권한")
			));
	}

	public static RestDocumentationResultHandler updateRole() {
		return document("user/updateRole",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			responseFields(
				fieldWithPath("code").type(JsonFieldType.STRING)
					.description("코드"),
				fieldWithPath("message").type(JsonFieldType.STRING)
					.description("메시지"),
				fieldWithPath("data").type(JsonFieldType.OBJECT)
					.description("응답 데이터"),
				fieldWithPath("data.id").type(JsonFieldType.NUMBER)
					.description("사용자 ID"),
				fieldWithPath("data.name").type(JsonFieldType.STRING)
					.description("사용자명"),
				fieldWithPath("data.point").type(JsonFieldType.NUMBER)
					.description("보유 포인트"),
				fieldWithPath("data.role").type(JsonFieldType.STRING)
					.description("사용자 권한")
			));

	}
}
