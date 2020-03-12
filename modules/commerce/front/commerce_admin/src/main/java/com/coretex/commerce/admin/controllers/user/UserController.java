package com.coretex.commerce.admin.controllers.user;

import com.coretex.commerce.admin.controllers.PageableDataTableAbstractController;
import com.coretex.commerce.admin.facades.UserFacade;
import com.coretex.commerce.data.GroupData;
import com.coretex.commerce.data.UserData;
import com.coretex.commerce.data.minimal.MinimalUserData;
import com.coretex.commerce.facades.GroupFacade;
import com.coretex.commerce.facades.PageableDataTableFacade;
import com.coretex.items.cx_core.UserItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController extends PageableDataTableAbstractController<MinimalUserData> {

	@Resource
	private UserFacade userFacade;
	@Resource
	private GroupFacade groupFacade;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String getUsers() {
		return "user/users";
	}

	@RequestMapping(path = "/account", method = RequestMethod.GET)
	public String getUserData(Model model) {
		model.addAttribute("user", getCurrentUser());
		return "account/profile";
	}

	@RequestMapping(path = "/account/{uuid}", method = RequestMethod.GET)
	public String getUserData(@PathVariable("uuid") UUID uuid, Model model) {
		var userByUUID = userFacade.getUserByUUID(uuid);
		model.addAttribute("user", userByUUID);
		return "account/profile";
	}

	@RequestMapping(path = "/account/save", method = RequestMethod.POST)
	public String saveUserData(@ModelAttribute("userForm") UserData userData, Model model){
		userFacade.saveUser(userData);

		model.addAttribute("user", userData);

		if(Objects.nonNull(userData.getUuid()) && !userData.getUuid().equals(getCurrentUser().getUuid())){
			redirect("/user/account/"+userData.getUuid());
		}
		return redirect("/user/account");
	}

	@ModelAttribute("groups")
	public Collection<GroupData> getGroups() {
		return groupFacade.getAll();
	}

	@Override
	protected PageableDataTableFacade<UserItem, MinimalUserData> getPageableFacade() {
		return userFacade;
	}
}
