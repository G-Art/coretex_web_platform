package com.coretex.commerce.admin.init.permission;

import com.coretex.items.cx_core.GroupItem;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

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
