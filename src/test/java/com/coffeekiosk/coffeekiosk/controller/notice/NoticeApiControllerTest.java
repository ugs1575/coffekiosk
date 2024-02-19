package com.coffeekiosk.coffeekiosk.controller.notice;

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
import org.springframework.security.test.context.support.WithMockUser;

import com.coffeekiosk.coffeekiosk.controller.RestDocsAndSecuritySupport;
import com.coffeekiosk.coffeekiosk.controller.notice.api.NoticeApiController;
import com.coffeekiosk.coffeekiosk.controller.notice.api.dto.request.NoticeSaveUpdateRequest;
import com.coffeekiosk.coffeekiosk.docs.notice.NoticeDocumentation;
import com.coffeekiosk.coffeekiosk.service.notice.NoticeService;
import com.coffeekiosk.coffeekiosk.service.notice.response.NoticeResponse;

@WebMvcTest(controllers = NoticeApiController.class)
class NoticeApiControllerTest extends RestDocsAndSecuritySupport {

	@MockBean
	protected NoticeService noticeService;

	@DisplayName("공지사항을 등록한다")
	@Test
	@WithMockUser(roles = "USER")
	void createNotice() throws Exception {
		//given
		NoticeSaveUpdateRequest request = NoticeSaveUpdateRequest.builder()
			.title("제목1")
			.content("내용1")
			.build();

		NoticeResponse response = NoticeResponse.builder()
			.id(1L)
			.title("제목1")
			.content("내용1")
			.build();

		when(noticeService.createNotice(any(), any(), any())).thenReturn(response);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.post("/api/notices", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.code").value("201"))
			.andExpect(jsonPath("$.message").value("CREATED"))
			.andExpect(header().string("Location", "/api/notices/1"))
			.andDo(NoticeDocumentation.createNotice());
	}

	@DisplayName("공지사항 등록 시 제목은 필수 값입니다.")
	@Test
	@WithMockUser(roles = "USER")
	void createPostWithoutTitle() throws Exception {
		//given
		NoticeSaveUpdateRequest request = NoticeSaveUpdateRequest.builder()
			.content("내용1")
			.build();

		//when //then
		mockMvc.perform(
				post("/api/notices", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("title"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("제목은 필수 입니다."));
	}

	@DisplayName("공지사항을 수정한다")
	@Test
	@WithMockUser(roles = "USER")
	void updateNotice() throws Exception {
		//given
		NoticeSaveUpdateRequest request = NoticeSaveUpdateRequest.builder()
			.title("제목1")
			.content("내용1")
			.build();

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/api/notices/{noticeId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(NoticeDocumentation.updateNotice());
	}

	@DisplayName("공지사항 수정 시 제목은 필수 값입니다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateNoticeWithoutTitle() throws Exception {
		//given
		NoticeSaveUpdateRequest request = NoticeSaveUpdateRequest.builder()
			.content("내용1")
			.build();

		//when //then
		mockMvc.perform(
				patch("/api/notices/{noticeId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("title"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("제목은 필수 입니다."));

	}

	@DisplayName("공지사항을 삭제한다.")
	@Test
	@WithMockUser(roles = "USER")
	void deleteNotice() throws Exception {
		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/notices/{noticeId}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(NoticeDocumentation.deleteNotice());
	}

	@DisplayName("공지사항 목록을 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void findNotices() throws Exception {
		//given
		NoticeResponse response = NoticeResponse.builder()
			.id(1L)
			.title("개업 이벤트")
			.content("모든 메뉴 10% 할인")
			.registeredDateTime(LocalDateTime.now())
			.userId(1L)
			.userName("우경서")
			.build();

		List<NoticeResponse> result = List.of(response);

		when(noticeService.findNotices()).thenReturn(result);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/notices")
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray())
			.andDo(NoticeDocumentation.findNotices());
	}

	@DisplayName("공지사항을 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void findNotice() throws Exception {
		//given
		NoticeResponse response = NoticeResponse.builder()
			.id(1L)
			.title("개업 이벤트")
			.content("모든 메뉴 10% 할인")
			.registeredDateTime(LocalDateTime.now())
			.userId(1L)
			.userName("우경서")
			.build();

		when(noticeService.findById(any())).thenReturn(response);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/notices/{noticeId}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(NoticeDocumentation.findNotice());
	}
}