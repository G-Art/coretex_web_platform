package com.coretex.commerce.admin.controllers.content;

import com.coretex.commerce.facades.ManufacturerFacade;
import com.coretex.commerce.facades.PageableDataTableFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.UUID;

@Controller
@RequestMapping("/manufacturer")
public class ManufactureController extends AbstractContentController {

	@Resource
	private ManufacturerFacade manufacturerFacade;

	@RequestMapping(path = "",method = RequestMethod.GET)
	public String getManufacturers(Model model) {
		return "product/manufacturers";
	}

	@RequestMapping(path = "/{uuid}", method = RequestMethod.GET)
	public String getManufacturer(@PathVariable("uuid") UUID uuid, Model model) {
		return "product/manufacture";
	}

	@RequestMapping(path = {"/remove/{uuid}"}, method = {RequestMethod.GET, RequestMethod.DELETE})
	public String removeManufacturer(@PathVariable(value = "uuid") UUID uuid,
	                          RedirectAttributes redirectAttributes) {
		manufacturerFacade.delete(uuid);
		addInfoFlashMessage(redirectAttributes, "Manufacturer removed");

		return redirect("/manufacturer");
	}

	@Override
	protected PageableDataTableFacade getPageableFacade() {
		return getManufacturerFacade();
	}


}
