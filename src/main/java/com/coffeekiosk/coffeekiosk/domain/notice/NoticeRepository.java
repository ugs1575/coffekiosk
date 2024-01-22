package com.coffeekiosk.coffeekiosk.domain.notice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	@Query("select n from Notice n join fetch n.user where n.id = :id")
	Optional<Notice> findByIdFetchJoin(@Param("id") Long id);
}
