package com.sample.ec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_customer")
public class EcCustomer {

	@Column(name = "customer_id")
	@Id
	private String customer_id;

	@Column(name = "customer_name")
	private String customer_name;

	@Column(name = "password")
	private String password;

	@Column(name = "phone_number")
	private String phone_number;

	@Column(name = "email")
	private String email;

	@Column(name = "memo")
	private String memo;

	public String getCustomerId() {
		return customer_id;
	}

	public void setCustomerId(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomerName() {
		return customer_name;
	}

	public void setCustomerName(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email= email;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}




}
