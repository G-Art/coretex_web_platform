package com.coretex.commerce.data;

import com.coretex.enums.cx_core.GroupTypeEnum;

public class GroupData extends GenericItemData {

	private String groupName;
	private GroupTypeEnum groupType;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public GroupTypeEnum getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupTypeEnum groupType) {
		this.groupType = groupType;
	}
}
