package com.coretex.shop.admin.data;

import com.coretex.enums.commerce_core_model.GroupTypeEnum;

public class GroupDto extends GenericDto {

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
