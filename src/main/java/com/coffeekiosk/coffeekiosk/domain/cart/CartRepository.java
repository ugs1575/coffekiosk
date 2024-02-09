package com.coffeekiosk.coffeekiosk.domain.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

	@Query("select c from Cart c join fetch c.user u join fetch c.item i where u.id = :userId and i.id = :itemId")
	Optional<Cart> findByUserIdAndItemIdFetchJoin(@Param("userId") Long userId, @Param("itemId") Long ItemId);

	@Query("select c from Cart c join fetch c.user u join fetch c.item i where c.id = :id")
	Optional<Cart> findByIdFetchJoin(@Param("id") Long id);
}
