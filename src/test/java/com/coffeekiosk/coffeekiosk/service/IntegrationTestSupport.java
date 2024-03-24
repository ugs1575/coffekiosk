package com.coffeekiosk.coffeekiosk.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.coffeekiosk.coffeekiosk.service.mail.MailService;

@SpringBootTest
public abstract class IntegrationTestSupport {

	@MockBean
	protected MailService mailService;
}
