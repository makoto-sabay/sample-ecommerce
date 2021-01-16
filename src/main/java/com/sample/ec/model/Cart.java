package com.sample.ec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(Cart.class)
@Table(name = "m_cart")
public class Cart implements Serializable {
	private static final long serialVersionUID = 7129287520258016188L;

	@Column(name = "customer_id")
	@Id
	private String customer_id;

	@Column(name = "product_id")
	@Id
	private String product_id;

	@Column(name ="customer_name")
	private String customer_name;

	@Column(name ="product_name")
	private String product_name;

	@Column(name ="qty")
	private int qty;

	@Column(name ="price")
	private int price;

	@Column(name ="sum")
	private int sum;

	@Column(name ="stock")
	private int stock;

	@Column(name ="cdate")
	private String cdate;

	public void setCustomerId(String customerId) {
		this.customer_id = customerId;
	}

	public String getCustomerId() {
		return customer_id;
	}

	public void setProductId(String productId) {
		this.product_id = productId;
	}

	public String getProductId() {
		return product_id;
	}

	public void setCustomerName(String customerName) {
		this.customer_name = customerName;
	}

	public String getCustomerName() {
		return customer_name;
	}

	public void setProductName(String productName) {
		this.product_name = productName;
	}

	public String getProductName() {
		return product_name;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getQty() {
		return qty;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public int getSum() {
		return sum;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStock() {
		return stock;
	}

	public void setCDate(String date) {
		this.cdate = date;
	}

	public String getCDate() {
		return cdate;
	}
}
