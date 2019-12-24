package com.coretex.commerce.admin.controllers.content;

import com.coretex.commerce.facades.PageableDataTableFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequestMapping("/manufacturer")
public class ManufactureController extends AbstractContentController {


	@RequestMapping(path = "",method = RequestMethod.GET)
	public String getProducts(Model model) {
		return "product/manufacturers";
	}

	@RequestMapping(path = "/{uuid}", method = RequestMethod.GET)
	public String getProduct(@PathVariable("uuid") UUID uuid, Model model) {
		return "product/manufacture";
	}

	@Override
	protected PageableDataTableFacade getPageableFacade() {
		return getManufacturerFacade();
	}


}
