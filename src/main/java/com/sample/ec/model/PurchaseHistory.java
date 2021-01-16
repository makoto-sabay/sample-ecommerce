package com.sample.ec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_purchase_history")
public class PurchaseHistory implements Serializable {

	@Column(name = "purchase_history_id")
	@Id
	private String purchase_history_id;

	@Column(name = "customer_id")
	private String customer_id;

	@Column(name = "customer_name")
	private String customer_name;

	@Column(name = "product_id")
	private String product_id;

	@Column(name = "product_name")
	private String product_name;

	@Column(name ="qty")
	private int qty;

	@Column(name ="price")
	private int price;

	@Column(name ="purchase_date")
	private String purchase_date;

	public void setPurchaseHistoryId(String ph_id) {
		this.purchase_history_id = ph_id;
	}

	public String getPurchaseHistoryId() {
		return purchase_history_id;
	}

	public void setCustomerId(String customerId) {
		this.customer_id = customerId;
	}

	public String getCustomerId() {
		return customer_name;
	}

	public void setCustomerName(String customerName) {
		this.customer_name = customerName;
	}

	public String getCustomerName() {
		return customer_name;
	}

	public void setProductId(String productId) {
		this.product_id = productId;
	}

	public String getProductId() {
		return product_id;
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

	public void setPurchaseDate(String date) {
		this.purchase_date = date;
	}

	public String getPurchaseDate() {
		return purchase_date;
	}
}
