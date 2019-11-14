package com.coretex.shop.admin.model.permission;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import com.coretex.items.commerce_core_model.GroupItem;

public class GroupDetails implements Serializable {


	private static final long serialVersionUID = 1L;
	@Valid
	private GroupItem group;
	private List<String> types;

	public GroupItem getGroup() {
		return group;
	}

	public void setGroup(GroupItem group) {
		this.group = group;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

}
