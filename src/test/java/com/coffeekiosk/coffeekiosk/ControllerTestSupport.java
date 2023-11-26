package com.coffeekiosk.coffeekiosk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.coffeekiosk.coffeekiosk.controller.ItemApiController;
import com.coffeekiosk.coffeekiosk.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ControllerTestSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;
}
