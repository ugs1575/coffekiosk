package com.coffeekiosk.coffeekiosk.domain.user;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.coffeekiosk.coffeekiosk.common.domain.BaseTimeEntity;
import com.coffeekiosk.coffeekiosk.common.exception.BusinessException;
import com.coffeekiosk.coffeekiosk.exception.ErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SQLDelete(sql = "UPDATE member SET deleted = true WHERE member_id=?")
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
	private int point;

	@Version
	private Long version;

	@Column(columnDefinition = "boolean default false")
	private boolean deleted;

	@Builder
	private User(String name, int point) {
		this.name = name;
		this.point = point;
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
}
