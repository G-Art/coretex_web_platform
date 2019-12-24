package com.coretex.commerce.data.minimal;

import com.coretex.commerce.data.GenericItemData;

public class MinimalLocaleData extends GenericItemData {

	private String isocode;

	private String name;

	public void setIsocode(final String isocode)
	{
		this.isocode = isocode;
	}

	public String getIsocode()
	{
		return isocode;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

}
