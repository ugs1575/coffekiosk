package com.coffeekiosk.coffeekiosk.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, Long> {

	@Lock(LockModeType.OPTIMISTIC)
	@Query("select u from User u where u.id = :id")
	Optional<User> findByIdWithOptimisticLock(@Param("id") Long Id);
}
