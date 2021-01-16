package com.sample.ec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "m_product")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "product_id", unique = true, nullable = false)
	private String product_id;

	@Column(name = "product_name", nullable = false)
	private String product_name;

	@Column(name = "genre", nullable = false)
	private String genre;

	@Column(name = "price")
	private int price;

	@Column(name = "overview")
	private String overview;

	@Column(name = "explanation1")
	private String explanation1;

	@Column(name = "explanation2")
	private String explanation2;

	@Column(name = "stock")
	private int stock;

	public void setProductId(String id) {
		this.product_id = id;
	}

	public String getProductId() {
		return product_id;
	}

	public void setProductName(String name) {
		this.product_name = name;
	}

	public String getProductName() {
		return product_name;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getGenre() {
		return genre;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getOverview() {
		return overview;
	}

	public void setExplanation1(String explanation1) {
		this.explanation1 = explanation1;
	}

	public String getExplanation1() {
		return explanation1;
	}

	public void setExplanation2(String explanation2) {
		this.explanation2 = explanation2;
	}

	public String getExplanation2() {
		return explanation2;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStock() {
		return stock;
	}
}
