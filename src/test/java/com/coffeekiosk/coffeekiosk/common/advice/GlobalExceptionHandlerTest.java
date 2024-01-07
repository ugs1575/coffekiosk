package com.coffeekiosk.coffeekiosk.common.advice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeekiosk.coffeekiosk.ControllerTestSupport;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;

import jakarta.validation.Valid;

@ContextConfiguration(classes = {GlobalExceptionHandlerTest.TestController.class, GlobalExceptionHandler.class})
@WebMvcTest(controllers = GlobalExceptionHandlerTest.TestController.class)
class GlobalExceptionHandlerTest extends ControllerTestSupport {
	
	@DisplayName("request body 형식에 맞지 않을 경우 예외를 처리한다.")
	@Test
	void handleHttpMessageNotReadableException() throws Exception {
		String requestJson = "{\"id\":\"test\"}";

		mockMvc.perform(
				post("/exception")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("id 필드 값의 타입이 잘못되었습니다."));
	}

	@DisplayName("맞지 않는 타입이 들어올 경우 예외를 처리한다.")
	@Test
	void handleMethodArgumentTypeMismatchException() throws Exception {
		String input = "test";

		mockMvc.perform(
				get("/exception/" + input)
					.accept(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.message").value("요청 값의 타입이 잘못되었습니다."))
			.andExpect(jsonPath("$.fieldErrors.[0].field").value("id"))
			.andExpect(jsonPath("$.fieldErrors.[0].message").value("요청 값의 타입이 잘못되었습니다."));
	}

	@DisplayName("잘못된 Http 메서드로 요청이 왔을 때 예외 처리한다.")
	@Test
	void handleHttpRequestMethodNotSupportedException() throws Exception {
		mockMvc.perform(delete("/exception"))
			.andDo(print())
			.andExpect(status().isMethodNotAllowed())
			.andExpect(jsonPath("$.code").value("405"))
			.andExpect(jsonPath("$.message").value("허용되지 않는 메서드입니다."));
	}

	@DisplayName("비즈니스 예외 처리한다.")
	@Test
	void handleBusinessException() throws Exception {
		mockMvc.perform(
				get("/exception/1")
					.accept(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value("404"))
			.andExpect(jsonPath("$.message").value("해당 데이터를 찾을 수 없습니다."));
	}

	@DisplayName("비즈니스 예외 이외의 예외를 처리한다.")
	@Test
	void handleException() throws Exception {
		mockMvc.perform(
				post("/exception")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(new TestDto(1)))
			)
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.code").value("500"))
			.andExpect(jsonPath("$.message").value("내부 서버 오류가 발생했습니다."));
	}

	private static class TestDto {
		private int id;

		public TestDto() {
		}

		public TestDto(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	@RestController
	@RequestMapping("/exception")
	static class TestController {
		@GetMapping("/{id}")
		public String executeBusinessException(@PathVariable Long id) {
			throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
		}

		@PostMapping
		public String executeException(@Valid @RequestBody TestDto testDto) {
			throw new RuntimeException();
		}
	}
}