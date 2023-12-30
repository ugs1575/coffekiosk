package com.coffeekiosk.coffeekiosk.controller.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.coffeekiosk.coffeekiosk.ControllerTestSupport;
import com.coffeekiosk.coffeekiosk.controller.user.dto.request.PointSaveRequest;
import com.coffeekiosk.coffeekiosk.service.user.PointService;

@WebMvcTest(controllers = PointApiController.class)
class PointApiControllerTest extends ControllerTestSupport {

	@MockBean
	protected PointService pointService;

	@DisplayName("포인트를 충전한다")
	@Test
	void savePoint() throws Exception {
	    //given
		PointSaveRequest request = PointSaveRequest.builder()
			.amount(10000)
			.build();

	    //when //then
		mockMvc.perform(
				post("/api/users/{userId}/points", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("최소 포인트 충전 금액은 10000원이다.")
	@Test
	void updateItemWithoutName() throws Exception {
		//given
		PointSaveRequest request = PointSaveRequest.builder()
			.amount(9000)
			.build();

		//when //then
		mockMvc.perform(
				post("/api/users/{userId}/points", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("적절하지 않은 요청 값입니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("amount"))
			.andExpect(jsonPath("$.fieldErrors.[0].value").value("9000"))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("충전 최소 금액은 10000원 입니다."));
	}
}