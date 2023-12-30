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
import com.coffeekiosk.coffeekiosk.service.user.UserService;

@WebMvcTest(controllers = UserApiController.class)
class UserApiControllerTest extends ControllerTestSupport {

	@MockBean
	protected UserService userService;

	@DisplayName("상품 상세정보를 조회한다.")
	@Test
	void findItem() throws Exception {
		//when //then
		mockMvc.perform(
				get("/api/users/{userId}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

}