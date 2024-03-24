package com.coffeekiosk.coffeekiosk.service.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.coffeekiosk.coffeekiosk.domain.order.Order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;

	public void sendOrderSuccessMail(String email, Order order) {
		System.out.println("메일 전송");
		Context context = new Context();
		context.setVariable("order", order);
		String subject = "[CoffeeKiosk] 고객님의 주문이 정상적으로 접수되었습니다.";
		String body = templateEngine.process("/mail/order-success.html", context);
		sendMail(email, subject, body);
	}

	private void sendMail(String to, String subject, String body) {
		try {
			MimeMessagePreparator messagePreparator =
				mimeMessage -> {
					final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					helper.setFrom("noreply@coffekiosk.com");
					helper.setTo(to);
					helper.setSubject(subject);
					helper.setText(body, true);
				};
			mailSender.send(messagePreparator);
		} catch (Exception e) {
			log.error("주문 성공 메일 발송 실패, 이메일 : {}", to);
		}
	}
}
