package com.coffeekiosk.coffeekiosk.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
					new AntPathRequestMatcher("/notice/**", HttpMethod.GET.name()),
					new AntPathRequestMatcher("/api/notice/**", HttpMethod.GET.name()),
					new AntPathRequestMatcher("/css/**"),
					new AntPathRequestMatcher("/images/**"),
					new AntPathRequestMatcher("/js/**"),
					new AntPathRequestMatcher("/h2-console/**")).permitAll()
				.requestMatchers(
					new AntPathRequestMatcher("/notice/**", HttpMethod.POST.name()),
					new AntPathRequestMatcher("/api/notice/**", HttpMethod.POST.name()),
					new AntPathRequestMatcher("/api/notice/**", HttpMethod.PATCH.name()),
					new AntPathRequestMatcher("/api/notice/**", HttpMethod.DELETE.name()),
					new AntPathRequestMatcher("/item/**"),
					new AntPathRequestMatcher("/api/items/**")
					).hasRole(Role.ADMIN.name())
				.requestMatchers(
					new AntPathRequestMatcher("/cart/**"),
					new AntPathRequestMatcher("/api/carts/**"),
					new AntPathRequestMatcher("/menu/**"),
					new AntPathRequestMatcher("/order/**"),
					new AntPathRequestMatcher("/api/orders/**"),
					new AntPathRequestMatcher("/point/**"),
					new AntPathRequestMatcher("/api/points/**")
				).hasRole(Role.USER.name())
				.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/")
			.and()
				.logout()
					.logoutSuccessUrl("/")
			.and()
				.exceptionHandling()
					.accessDeniedPage("/access-denied")
			.and()
				.oauth2Login()
					.userInfoEndpoint()
						.userService(customOAuth2UserService);

		return http.build();
	}

}