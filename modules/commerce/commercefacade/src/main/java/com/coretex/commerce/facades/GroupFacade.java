package com.coretex.commerce.facades;

import com.coretex.commerce.data.GroupData;

import java.util.List;

public interface GroupFacade {
	GroupData getByName(String name);

	List<GroupData> getAll();
}
