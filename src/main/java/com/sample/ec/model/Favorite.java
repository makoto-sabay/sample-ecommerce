package com.sample.ec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(Favorite.class)
@Table(name = "m_favorite")
public class Favorite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "customer_id")
	@Id
	private String customer_id;

	@Column(name = "product_id")
	@Id
	private String product_id;

	@Column(name ="fdate")
	private String fdate;

	public void setCustomerId(String id) {
		this.customer_id = id;
	}

	public String getCustomerId() {
		return customer_id;
	}

	public void setProductId(String id) {
		this.product_id = id;
	}

	public String getProductId() {
		return product_id;
	}

	public void setDate(String date) {
		this.fdate = date;
	}

	public String getDate() {
		return fdate;
	}
}
