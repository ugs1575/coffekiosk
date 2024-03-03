package com.coffeekiosk.coffeekiosk.controller;

import static org.hamcrest.core.StringContains.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = ProfileController.class)
public class ProfileControllerTest extends RestDocsAndSecuritySupport {

	@Test
	public void profile은_인증없이_호출된다() throws Exception {
		String expected = "test";

		mockMvc.perform(
				get("/profile")
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(expected)));
	}
}
