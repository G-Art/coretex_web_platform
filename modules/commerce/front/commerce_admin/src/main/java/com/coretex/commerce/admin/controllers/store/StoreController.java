package com.coretex.commerce.admin.controllers.store;

import com.coretex.commerce.admin.controllers.PageableDataTableAbstractController;
import com.coretex.commerce.data.minimal.MinimalStoreData;
import com.coretex.commerce.facades.PageableDataTableFacade;
import com.coretex.commerce.facades.StoreFacade;
import com.coretex.items.cx_core.StoreItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.UUID;

@Controller
@RequestMapping("/store")
public class StoreController extends PageableDataTableAbstractController<MinimalStoreData> {

	@Resource
	private StoreFacade storeFacade;

	@RequestMapping(path = "",method = RequestMethod.GET)
	public String getStores(Model model) {
		return "store/stores";
	}

	@RequestMapping(path = "/{uuid}", method = RequestMethod.GET)
	public String getStore(@PathVariable("uuid") UUID uuid, Model model) {
		model.addAttribute("store", storeFacade.getByUuid(uuid));
		return "store/store";
	}

	@RequestMapping(path = {"/remove/{uuid}"}, method = {RequestMethod.GET, RequestMethod.DELETE})
	public String removeStore(@PathVariable(value = "uuid") UUID uuid,
	                            RedirectAttributes redirectAttributes) {
		storeFacade.delete(uuid);
		addInfoFlashMessage(redirectAttributes, "Store removed");

		return redirect("/store");
	}

	@Override
	protected PageableDataTableFacade<StoreItem, MinimalStoreData> getPageableFacade() {
		return storeFacade;
	}
}
