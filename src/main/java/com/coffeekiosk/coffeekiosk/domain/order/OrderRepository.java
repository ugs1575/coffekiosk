package com.coffeekiosk.coffeekiosk.domain.order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("select o from Order o join fetch o.orderItems.orderItems oi join fetch oi.item i where o.id = :id")
	Optional<Order> findByIdFetchJoin(@Param("id") Long id);
}
