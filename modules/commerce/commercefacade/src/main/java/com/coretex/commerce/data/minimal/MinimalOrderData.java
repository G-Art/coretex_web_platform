package com.coretex.commerce.data.minimal;

import com.coretex.commerce.data.GenericItemData;

public class MinimalOrderData extends GenericItemData {

	private double total;
	private String name;
	private String status;
	private String date;
	private String store;

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}
}
