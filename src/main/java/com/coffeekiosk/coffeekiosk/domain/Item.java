package com.coffeekiosk.coffeekiosk.domain;

import java.time.LocalDateTime;

import com.coffeekiosk.coffeekiosk.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "items")
public class Item extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private ItemType itemType;

	private int price;

	private LocalDateTime lastModifiedDateTime;

	@Builder
	private Item(String name, ItemType itemType, int price, LocalDateTime lastModifiedDateTime) {
		this.name = name;
		this.itemType = itemType;
		this.price = price;
		this.lastModifiedDateTime = lastModifiedDateTime;
	}
}
