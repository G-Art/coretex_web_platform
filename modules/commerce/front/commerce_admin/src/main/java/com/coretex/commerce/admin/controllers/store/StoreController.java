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
		return "store/store";
	}

	@Override
	protected PageableDataTableFacade<StoreItem, MinimalStoreData> getPageableFacade() {
		return storeFacade;
	}
}
