package com.sample.ec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sample.ec.model.RegistrationUser;

@Transactional
public interface RegistrationRepository extends JpaRepository<RegistrationUser, Integer>{
	RegistrationUser findByCustomerName(String customer_name);
	RegistrationUser findByEmail(String email);

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
    @Query("update RegistrationUser u set u.customerName = ?1, u.password = ?2, u.phoneNumber = ?3, u.email = ?4, u.realName = ?5,  u.address1 = ?6, u.cardNum = ?7 where u.customerId = ?8")
	void updateUser(String customerName, String password, String phoneNumber, String email, String realName, String address1, String cardNum, String customerId);
}
