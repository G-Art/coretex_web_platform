package com.coretex.shop.utils;

import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.cx_core.UserItem;

import java.util.List;

public class UserUtils {

	public static boolean userInGroup(UserItem user, String groupName) {


		List<GroupItem> logedInUserGroups = user.getGroups();
		for (GroupItem group : logedInUserGroups) {
			if (group.getGroupName().equals(groupName)) {
				return true;
			}
		}

		return false;

	}

}
