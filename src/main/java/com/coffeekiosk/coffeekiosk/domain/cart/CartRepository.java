package com.coffeekiosk.coffeekiosk.domain.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coffeekiosk.coffeekiosk.domain.item.Item;
import com.coffeekiosk.coffeekiosk.domain.user.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

	@Query("select c from Cart c join fetch c.item i where c.id in :ids and c.user.id = :userId")
	List<Cart> findByIdInFetchJoin(@Param("ids") List<Long> ids, @Param("userId") Long userId);

	@Query("select c from Cart c join fetch c.item i where c.user.id = :userId")
	List<Cart> findByUserIdFetchJoin(@Param("userId") Long userId);

	@Modifying
	@Query("delete from Cart c where c.id in :ids and c.user.id = :userId")
	void deleteByIdIn(@Param("ids") List<Long> ids, @Param("userId") Long userId);

	@Modifying
	@Query("delete from Cart c where c.id = :id and c.user.id = :userId")
	void deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

	Cart findByUserAndItem(User user, Item item);
}
