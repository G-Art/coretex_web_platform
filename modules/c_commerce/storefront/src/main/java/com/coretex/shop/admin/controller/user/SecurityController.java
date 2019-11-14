package com.coretex.shop.admin.controller.user;

import com.coretex.core.business.services.user.GroupService;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;
import com.coretex.core.data.web.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SecurityController {

	@Resource
	GroupService groupService;

	@RequestMapping(value = "/admin/user/permissions.html", method = RequestMethod.GET)
	public String displayPermissions(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);
		return "admin-user-permissions";


	}


	@RequestMapping(value = "/admin/user/groups.html", method = RequestMethod.GET)
	public String displayGroups(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);
		List<GroupItem> groups = groupService.listGroup(GroupTypeEnum.ADMIN);

		model.addAttribute("groups", groups);

		return "admin-user-groups";


	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("profile", "profile");
		activeMenus.put("security", "security");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("profile");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
