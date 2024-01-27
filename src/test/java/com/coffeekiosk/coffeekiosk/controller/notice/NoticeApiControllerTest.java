package com.coffeekiosk.coffeekiosk.controller.notice;

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
import com.coffeekiosk.coffeekiosk.controller.notice.dto.request.NoticeSaveUpdateRequest;
import com.coffeekiosk.coffeekiosk.service.notice.NoticeService;
import com.coffeekiosk.coffeekiosk.service.notice.response.NoticeResponse;

@WebMvcTest(controllers = NoticeApiController.class)
class NoticeApiControllerTest extends ControllerTestSupport {

	@MockBean
	protected NoticeService noticeService;

	@DisplayName("공지사항을 등록한다")
	@Test
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
				post("/api/users/{userId}/notices", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data.id").value(1L));
	}

	@DisplayName("공지사항 등록 시 제목은 필수 값입니다.")
	@Test
	void createPostWithoutTitle() throws Exception {
		//given
		NoticeSaveUpdateRequest request = NoticeSaveUpdateRequest.builder()
			.content("내용1")
			.build();

		//when //then
		mockMvc.perform(
				post("/api/users/{userId}/notices", 1L)
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
	void updateNotice() throws Exception {
		//given
		NoticeSaveUpdateRequest request = NoticeSaveUpdateRequest.builder()
			.title("제목1")
			.content("내용1")
			.build();

		//when //then
		mockMvc.perform(
				patch("/api/notices/{noticeId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("공지사항 수정 시 제목은 필수 값입니다.")
	@Test
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
	void deleteNotice() throws Exception {
		//when //then
		mockMvc.perform(
				delete("/api/notices/{noticeId}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("공지사항 목록을 조회한다.")
	@Test
	void findNotices() throws Exception {
		//given
		List<NoticeResponse> result = List.of();

		when(noticeService.findNotices()).thenReturn(result);

		//when //then
		mockMvc.perform(
				get("/api/notices")
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray());
	}

	@DisplayName("공지사항을 조회한다.")
	@Test
	void findNotice() throws Exception {
		//when //then
		mockMvc.perform(
				get("/api/notices/{noticeId}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
}