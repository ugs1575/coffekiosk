package com.coffeekiosk.coffeekiosk.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.coffeekiosk.coffeekiosk.config.auth.CustomOAuth2UserService;
import com.coffeekiosk.coffeekiosk.domain.user.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.headers().frameOptions().disable()
			.and()
				.authorizeHttpRequests()
				.requestMatchers(
					new AntPathRequestMatcher("/"),
					new AntPathRequestMatcher("/css/**"),
					new AntPathRequestMatcher("/images/**"),
					new AntPathRequestMatcher("/js/**"),
					new AntPathRequestMatcher("/h2-console/**")
				).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/api/**")).hasRole(Role.USER.name())
				.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/")
			.and()
				.logout()
					.logoutSuccessUrl("/")
			.and()
				.oauth2Login()
					.userInfoEndpoint()
						.userService(customOAuth2UserService);

		return http.build();
	}

}