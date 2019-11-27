package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.data.GroupData;

import java.util.List;

public interface GroupFacade {
	GroupData getByName(String name);

	List<GroupData> getAll();
}
