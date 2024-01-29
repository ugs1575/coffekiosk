package com.coffeekiosk.coffeekiosk.docs.notice;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

public class NoticeDocumentation {
	public static RestDocumentationResultHandler createNotice() {
		return document("notice/create",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("userId").description("사용자 ID")
			),
			requestFields(
				fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
				fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용").optional()
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
				headerWithName("Location").description("생성된 공지사항 ID")
			)
		);
	}

	public static RestDocumentationResultHandler updateNotice() {
		return document("notice/update",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("noticeId").description("공지사항 ID")
			),
			requestFields(
				fieldWithPath("title").type(JsonFieldType.STRING).description("공지사항 제목"),
				fieldWithPath("content").type(JsonFieldType.STRING).description("공지사항 내용").optional()
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

	public static RestDocumentationResultHandler deleteNotice() {
		return document("notice/delete",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("noticeId").description("공지사항 ID")
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

	public static RestDocumentationResultHandler findNotice() {
		return document("notice/find",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			pathParameters(
				parameterWithName("noticeId").description("공지사항 ID")
			),
			responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER)
					.description("코드"),
				fieldWithPath("message").type(JsonFieldType.STRING)
					.description("메시지"),
				fieldWithPath("data").type(JsonFieldType.OBJECT)
					.description("응답 데이터"),
				fieldWithPath("data.id").type(JsonFieldType.NUMBER)
					.description("공지사항 ID"),
				fieldWithPath("data.title").type(JsonFieldType.STRING)
					.description("공지사항 제목"),
				fieldWithPath("data.content").type(JsonFieldType.STRING)
					.description("공지사항 내용"),
				fieldWithPath("data.registeredDateTime").type(JsonFieldType.STRING)
					.description("공지사항 등록일"),
				fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
					.description("공자사항 작성자 ID"),
				fieldWithPath("data.userName").type(JsonFieldType.STRING)
					.description("공자사항 작성자 명")
			));
	}

	public static RestDocumentationResultHandler findNotices() {
		return document("notice/findAll",
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
					.description("공지사항 ID"),
				fieldWithPath("data.[].title").type(JsonFieldType.STRING)
					.description("공지사항 제목"),
				fieldWithPath("data.[].content").type(JsonFieldType.STRING)
					.description("공지사항 내용"),
				fieldWithPath("data.[].registeredDateTime").type(JsonFieldType.STRING)
					.description("공지사항 등록일"),
				fieldWithPath("data.[].userId").type(JsonFieldType.NUMBER)
					.description("공자사항 작성자 ID"),
				fieldWithPath("data.[].userName").type(JsonFieldType.STRING)
					.description("공자사항 작성자 명")
			));
	}
}
