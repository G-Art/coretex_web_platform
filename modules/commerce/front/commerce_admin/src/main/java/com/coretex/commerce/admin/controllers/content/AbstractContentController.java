package com.coretex.commerce.admin.controllers.content;

import com.coretex.commerce.admin.controllers.PageableDataTableAbstractController;
import com.coretex.commerce.data.GenericItemData;
import com.coretex.commerce.facades.CategoryFacade;
import com.coretex.commerce.facades.ManufacturerFacade;
import com.coretex.commerce.facades.ProductFacade;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;

public abstract class AbstractContentController<D extends GenericItemData> extends PageableDataTableAbstractController<D> {

	@Resource
	private CategoryFacade categoryFacade;

	@Resource
	private ProductFacade productFacade;

	@Resource
	private ManufacturerFacade manufacturerFacade;


	@ModelAttribute("categoriesCount")
	public Long getCategoriesCount() {
		return categoryFacade.count();
	}

	@ModelAttribute("productsCount")
	public Long getProductsCount() {
		return productFacade.count();
	}

	@ModelAttribute("manufacturerCount")
	public Long getManufacturersCount() {
		return manufacturerFacade.count();
	}

	protected CategoryFacade getCategoryFacade() {
		return categoryFacade;
	}

	protected ProductFacade getProductFacade() {
		return productFacade;
	}

	protected ManufacturerFacade getManufacturerFacade() {
		return manufacturerFacade;
	}
}
