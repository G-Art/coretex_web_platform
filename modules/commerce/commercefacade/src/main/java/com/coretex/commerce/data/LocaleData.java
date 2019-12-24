package com.coretex.commerce.data;

import com.coretex.commerce.data.minimal.MinimalLocaleData;

public class LocaleData extends MinimalLocaleData {

	private boolean active;

	public void setActive(final boolean active)
	{
		this.active = active;
	}

	public boolean isActive()
	{
		return active;
	}

}
