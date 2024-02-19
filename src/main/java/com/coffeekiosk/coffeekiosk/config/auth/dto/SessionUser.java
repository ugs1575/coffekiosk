package com.coffeekiosk.coffeekiosk.config.auth.dto;

import java.io.Serializable;

import com.coffeekiosk.coffeekiosk.domain.user.User;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
	private String name;
	private String email;
	private String picture;

	public SessionUser(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.picture = user.getPicture();
	}
}
