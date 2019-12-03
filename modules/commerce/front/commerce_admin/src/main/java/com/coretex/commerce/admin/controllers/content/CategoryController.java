package com.coretex.commerce.admin.controllers.content;

import com.coretex.commerce.admin.controllers.AbstractController;
import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.controllers.PageableDataTableAbstractController;
import com.coretex.commerce.admin.data.CategoryHierarchyData;
import com.coretex.commerce.admin.data.MinimalCategoryData;
import com.coretex.commerce.admin.facades.CategoryFacade;
import com.coretex.commerce.admin.facades.PageableDataTableFacade;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractContentController {


	@RequestMapping(path = "",method = RequestMethod.GET)
	public String getCategories() {
		return "category/categories";
	}

	@RequestMapping(path = "/hierarchy",method = RequestMethod.GET)
	public String getCategoryHierarchy() {
		return "category/categoryHierarchy";
	}

	@RequestMapping(path = {"/parent/{parent}", "/parent/"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void getCategoryHierarchyByUUID(@PathVariable(value = "parent", required = false) UUID parent, @RequestParam("category") UUID category) {
		getCategoryFacade().setParent(category, parent);
	}

	@RequestMapping(path = {"/h/{uuid}", "/h", "/h/"},method = RequestMethod.GET)
	@ResponseBody
	public List<CategoryHierarchyData> getCategoryHierarchyByUUID(@PathVariable(value = "uuid",
			required = false) UUID uuid) {
		return getCategoryFacade().categoryHierarchyLeverByNodeUUID(uuid);
	}


	@Override
	protected PageableDataTableFacade getPageableFacade() {
		return getCategoryFacade();
	}
}
