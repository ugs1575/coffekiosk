package com.coffeekiosk.coffeekiosk;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.coffeekiosk.coffeekiosk.config.auth")
public class SecurityDocumentationConfig {
}
