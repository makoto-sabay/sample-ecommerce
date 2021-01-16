package com.sample.ec.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "m_customer")
public class RegistrationUser {

	@Column(name = "customer_id")
	@Id
	private String customerId;

	@Column(name = "customer_name")
    @Length(min = 3, message = "*The name must have at least 3 characters.")
    @NotEmpty(message = "*Please provide your nickname.")
	private String customerName;

	@Column(name = "password")
	@Length(min = 8, message = "*The password must have at least 8 characters.")
    @NotEmpty(message = "*Please provide your password.")
	private String password;

	@Column(name = "phone_number")
    @NotEmpty(message = "*Please provide your phone number.")
	private String phoneNumber;

	@Email(message = "*Please provide your valid Email.")
    @NotEmpty(message = "*Please provide your email.")
	@Column(name = "email")
	private String email;

    @NotEmpty(message = "*Please provide your realname.")
	@Column(name = "real_name")
	private String realName;

    @NotEmpty(message = "*Please provide your card number.")
	@Column(name = "card_num")
	private String cardNum;

	@Column(name = "memo")
	private String memo;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "address1")
    @NotEmpty(message = "*Please provide your address.")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "customer_role", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customer_id) {
		this.customerId = customer_id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phone_number) {
		this.phoneNumber = phone_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email= email;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setActive(boolean b) {
		this.active = b;
	}

	public boolean getActive() {
		return active;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}
}
