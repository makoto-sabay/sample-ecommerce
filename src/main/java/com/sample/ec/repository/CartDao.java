package com.sample.ec.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.ec.model.Cart;

/**
 * Data Access Object
 * Call an access method to database
 *
 * @author Makoto
 */
@Repository
public class CartDao {
	private String GET_CART_DATA_QUERY = "SELECT * FROM m_cart WHERE customer_id = :customerId AND product_id = :productId";
	private String FIND_CART_QUERY = "SELECT * FROM m_cart WHERE customer_id = :customerId ";

	@Autowired
	EntityManager em;

	public List<Cart> findCartList(String customerId) throws NoResultException {
		List<Cart> cList = this.em.createNativeQuery((FIND_CART_QUERY), Cart.class)
		 .setParameter("customerId", customerId)
		 .getResultList();
		return cList;
	}

	public Cart getCart(String customerId, String productId) throws NoResultException {
		Cart cart = (Cart)em.createNativeQuery(GET_CART_DATA_QUERY, Cart.class)
		 .setParameter("customerId", customerId)
		 .setParameter("productId", productId)
		 .getSingleResult();
		return cart;
	}
}
