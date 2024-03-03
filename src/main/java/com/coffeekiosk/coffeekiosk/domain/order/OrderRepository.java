package com.coffeekiosk.coffeekiosk.domain.order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
	@Query("select o from Order o "
		+ "join fetch o.orderItems.orderItems oi "
		+ "join fetch oi.item i "
		+ "join fetch o.user u "
		+ "where o.id = :orderId and u.id = :userId")
	Optional<Order> findByIdFetchJoin(@Param("orderId") Long orderId, @Param("userId") Long userId);
}
