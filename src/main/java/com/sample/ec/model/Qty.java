package com.sample.ec.model;

import java.io.Serializable;

public class Qty implements Serializable {
	private int qty;

	public void setQty(int qty){
		this.qty = qty;
	}

	public int getQty() {
		return qty;
	}
}
