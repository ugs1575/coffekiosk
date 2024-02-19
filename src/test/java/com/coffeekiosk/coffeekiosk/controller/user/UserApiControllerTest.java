package com.coffeekiosk.coffeekiosk.controller.user;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import com.coffeekiosk.coffeekiosk.controller.RestDocsAndSecuritySupport;
import com.coffeekiosk.coffeekiosk.controller.user.api.UserApiController;
import com.coffeekiosk.coffeekiosk.docs.user.UserDocumentation;
import com.coffeekiosk.coffeekiosk.service.user.UserService;
import com.coffeekiosk.coffeekiosk.service.user.dto.response.UserResponse;

@WebMvcTest(controllers = UserApiController.class)
class UserApiControllerTest extends RestDocsAndSecuritySupport {

	@MockBean
	protected UserService userService;

	@DisplayName("상품 상세정보를 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void findItem() throws Exception {
		//given
		UserResponse response = UserResponse.builder()
			.id(1L)
			.name("우경서")
			.point(10000)
			.build();

		when(userService.findUser(any())).thenReturn(response);

		//when //then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/me")
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(UserDocumentation.findUser());
	}

}