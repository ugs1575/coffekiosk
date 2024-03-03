package com.coffeekiosk.coffeekiosk.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.coffeekiosk.coffeekiosk.common.domain.BaseTimeEntity;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column
	private String picture;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable = false)
	private int point;

	@Column(columnDefinition = "boolean default false")
	private boolean deleted;

	@Builder
	private User(String name, String email, String picture, Role role, int point) {
		this.name = name;
		this.email = email;
		this.picture = picture;
		this.role = role;
		this.point = point;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}

	public void savePoint(int amount) {
		this.point += amount;
	}

	public void deductPoint(int totalPrice) {
		if (isMoreThanCurrentPoint(totalPrice)) {
			throw new BusinessException(ErrorCode.INSUFFICIENT_POINT);
		}

		point -= totalPrice;
	}

	public boolean isMoreThanCurrentPoint(int totalPrice) {
		return totalPrice > point;
	}

	public User update(String name, String picture) {
		this.name = name;
		this.picture = picture;

		return this;
	}

	public void updateRole(Role role) {
		this.role = role;
	}
}
