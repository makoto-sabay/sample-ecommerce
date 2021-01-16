package com.sample.ec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sample.ec.model.Cart;

@Transactional
public interface CartRepository extends JpaRepository<Cart, Integer> {

	/**
	 * Update Quantity
	 *
	 * incorrect : m_cart (Table Name)
	 * correct: Cart (Entity Class)
	 *
	 * @param qty
	 * @param customerId
	 * @param productId
	 */
    @Modifying
    @Query("update Cart c set c.qty = ?1, c.sum = ?2 where c.customer_id = ?3 and c.product_id = ?4")
    public void updateQtyInCart(int qty, int sum, String customerId, String productId);
}
