package com.coffeekiosk.coffeekiosk.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.item.ItemRepositoryCustom;

public interface UserRepository extends JpaRepository<User, Long> {
}
