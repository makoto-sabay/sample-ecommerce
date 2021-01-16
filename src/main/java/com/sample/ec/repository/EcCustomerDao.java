package com.sample.ec.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.ec.model.EcCustomer;
import com.sample.ec.model.RegistrationUser;

/**
 * Data Access Object
 * Call an access method to database
 *
 * @author Makoto
 */
@Repository
public class EcCustomerDao {
	private String FIND_CUSTOMER_QUERY = "SELECT * FROM m_customer WHERE customer_id = :customerId ";
	private String CHECK_SAME_USER_QUERY = "SELECT * FROM m_customer WHERE customer_name = :customerName";
	private String CREATE_NEW_USERID_QUERY = "SELECT customer_id FROM m_customer";
	private String FIND_CUSTOMER_ID_QUERY = "SELECT customer_id FROM m_customer WHERE customer_name = :customerName ";
	private String GET_CUSTOMER_QUERY = "SELECT * FROM m_customer WHERE customer_name = :customerName";

	@Autowired
	EntityManager em;

	/**
	 * Search Customer
	 *
	 *  We search for registered users using the customer ID user inputs.
	 *  If the customer id exists, it returns the UserEntity. But if not, it returns null.
	 *  We need to cast the result that we get from Object to the EcCustomer class.
	 *
	 * @param customerId
	 * @return UserEntity or Null
	 */
	public EcCustomer findCustomer(String customerId) {
		return (EcCustomer)em.createNativeQuery(FIND_CUSTOMER_QUERY, EcCustomer.class).setParameter("customerId", customerId).getSingleResult();
	}

	/**
	 * Get Same User
	 *
	 *  We check for registered users.
	 *
	 * @param UserEntity
	 * @return
	 */
	public EcCustomer getSameUser(RegistrationUser user) {
		EcCustomer userExist = null;
		String customerName = user.getCustomerName();

		try {
			userExist = (EcCustomer)em.createNativeQuery(CHECK_SAME_USER_QUERY, EcCustomer.class)
			 .setParameter("customerName", customerName)
			 .getSingleResult();
		}
		catch(NoResultException nre) {
			userExist = null;
		}
		return userExist;
	}


	/**
	 * Get All Customer ID List
	 *
	 * @return ID List (String)
	 */
	public List<String> getAllCustomerIDList() {
		List<String> idList = ((Query)em.createNativeQuery(CREATE_NEW_USERID_QUERY)).getResultList();
		return idList;
	}


	/**
	 * Get Customer by a customer name.
	 *
	 * @param customerName
	 * @return
	 */
	public EcCustomer getCustomer(String customerName) {
		EcCustomer customer = null;
		try {
			customer =  (EcCustomer)em.createNativeQuery(GET_CUSTOMER_QUERY, EcCustomer.class)
				.setParameter("customerName", customerName)
				.getSingleResult();
		}
		catch(NoResultException nre) {
			nre.printStackTrace();
		}
		return customer;
	}


	/**
	 * Get Customer ID
	 *
	 * @param customerName
	 * @return
	 */
	public String getCustomerId(String customerName) {
		String customerId = "";
		try {
			customerId = (String)em.createNativeQuery(FIND_CUSTOMER_ID_QUERY)
			 .setParameter("customerName", customerName)
			 .getSingleResult();
		}
		catch(NoResultException nre) {
			nre.printStackTrace();
		}
		return customerId;
	}


}
