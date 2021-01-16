package com.sample.ec.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.ec.model.Product;

/**
 * Data Access Object
 * Call an access method to database
 *
 * @author Makoto
 */
@Repository
public class ProductDao {
	private String GET_PRODUCT_QUERY = "SELECT * FROM m_product WHERE product_id = :productId ";
	private String GET_PRODUCT_ID_LIST_QUERY = "SELECT product_id FROM m_product";

	@Autowired
	EntityManager em;

	/**
	 * Get Product by Product ID.
	 *
	 * @param productId
	 * @return
	 */
	public Product getProduct(String productId) {
		return (Product)em.createNativeQuery(GET_PRODUCT_QUERY, Product.class).setParameter("productId", productId).getSingleResult();
	}

	/**
	 * Get ALL Product ID
	 *
	 * @return product list
	 */
	public List<String> getProductIdList() {
		return (em.createNativeQuery(GET_PRODUCT_ID_LIST_QUERY)).getResultList();
	}

}
