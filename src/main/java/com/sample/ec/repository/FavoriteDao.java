package com.sample.ec.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.ec.model.Favorite;

/**
 * Data Access Object
 * Call an access method to database
 *
 * @author Makoto
 */
@Repository
public class FavoriteDao {
	private String GET_FAVRITE_DATA_QUERY = "SELECT * FROM m_favorite WHERE customer_id = :customerId AND product_id = :productId";
	private String GET_FAVORITE_LIST_QUERY = "SELECT * FROM m_favorite WHERE customer_id = :customerId";

	@Autowired
	EntityManager em;

	/**
	 * Get Favorite Data
	 *
	 * @param customerId
	 * @param productId
	 * @return
	 */
	public Favorite getFavoriteData(String customerId, String productId) throws NoResultException {
		Favorite fData = (Favorite)em.createNativeQuery(GET_FAVRITE_DATA_QUERY, Favorite.class)
		 .setParameter("customerId", customerId)
		 .setParameter("productId", productId)
		 .getSingleResult();
		return fData;
	}


	/**
	 * Get Favorite List by Customer ID
	 *
	 * @param customerId
	 * @return
	 * @throws NoResultException
	 */
	public List<Favorite> getFavoriteList(String customerId) throws NoResultException {
		List<Favorite> fList = this.em.createNativeQuery(GET_FAVORITE_LIST_QUERY, Favorite.class)
		 .setParameter("customerId", customerId)
		 .getResultList();
		return fList;
	}

}
