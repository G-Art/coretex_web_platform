package com.coretex.core.model.user;

import com.coretex.items.commerce_core_model.PermissionItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PermissionList implements Serializable {


	private static final long serialVersionUID = -3122326940968441727L;
	private int totalCount;
	private List<PermissionItem> permissions = new ArrayList<PermissionItem>();

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<PermissionItem> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionItem> permissions) {
		this.permissions = permissions;
	}

}
