package com.coretex.commerce.admin.data;

public class LocaleData extends GenericItemData {

	private String isocode;

	private String name;

	private boolean active;

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

	public void setActive(final boolean active)
	{
		this.active = active;
	}

	public boolean isActive()
	{
		return active;
	}

}
