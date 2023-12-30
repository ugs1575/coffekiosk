package com.coffeekiosk.coffeekiosk.domain.user;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.coffeekiosk.coffeekiosk.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

	private int point;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean deleted;

	@Builder
	private User(String name, int point) {
		this.name = name;
		this.point = point;
	}

	public void savePoint(int amount) {
		this.point += amount;
	}
}
