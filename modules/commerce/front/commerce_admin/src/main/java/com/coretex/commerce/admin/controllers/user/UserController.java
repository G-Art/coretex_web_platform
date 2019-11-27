package com.coretex.commerce.admin.controllers.user;

import com.coretex.commerce.admin.controllers.AbstractController;
import com.coretex.commerce.admin.data.GroupData;
import com.coretex.commerce.admin.data.MerchantStoreData;
import com.coretex.commerce.admin.data.UserData;
import com.coretex.commerce.admin.facades.GroupFacade;
import com.coretex.commerce.admin.facades.StoreFacade;
import com.coretex.commerce.admin.facades.UserFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/user/account")
public class UserController extends AbstractController {

	@Resource
	private UserFacade userFacade;
	@Resource
	private StoreFacade storeFacade;
	@Resource
	private GroupFacade groupFacade;

	@RequestMapping(method = RequestMethod.GET)
	public String getUserData(Model model) {
		model.addAttribute("user", getCurrentUser());
		return "account/profile";
	}

	@RequestMapping(path = "/{uuid}", method = RequestMethod.GET)
	public String getUserData(@PathVariable("uuid") UUID uuid, Model model) {
		var userByUUID = userFacade.getUserByUUID(uuid);
		model.addAttribute("user", userByUUID);
		return "account/profile";
	}
	@RequestMapping(path = "/save", method = RequestMethod.POST)
	public String saveUserData(@ModelAttribute("userForm") UserData userData, Model model){
		userFacade.saveUser(userData);

		model.addAttribute("user", userData);

		if(Objects.nonNull(userData.getUuid()) && !userData.getUuid().equals(getCurrentUser().getUuid())){
			redirect("/user/account/"+userData.getUuid());
		}
		return redirect("/user/account");
	}


	@ModelAttribute("stores")
	public Collection<MerchantStoreData> getStores() {
		return storeFacade.getAll();
	}

	@ModelAttribute("groups")
	public Collection<GroupData> getGroups() {
		return groupFacade.getAll();
	}
}
