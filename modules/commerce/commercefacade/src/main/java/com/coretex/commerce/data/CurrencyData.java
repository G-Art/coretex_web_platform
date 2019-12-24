package com.coretex.commerce.data;

public class CurrencyData extends GenericItemData {

	private String isocode;
	private String name;
	private boolean active;
	private String symbol;

	public void setIsocode(final String isocode) {
		this.isocode = isocode;
	}

	public String getIsocode() {
		return isocode;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setSymbol(final String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

}