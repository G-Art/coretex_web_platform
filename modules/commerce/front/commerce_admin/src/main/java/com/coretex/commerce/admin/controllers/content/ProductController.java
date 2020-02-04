package com.coretex.commerce.admin.controllers.content;

import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.facades.PageableDataTableFacade;
import com.coretex.items.cx_core.ProductItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractContentController<MinimalProductData> {


	@RequestMapping(path = "",method = RequestMethod.GET)
	public String getProducts(Model model) {
		return "product/products";
	}

	@RequestMapping(path = "/{uuid}", method = RequestMethod.GET)
	public String getProduct(@PathVariable("uuid") UUID uuid, Model model) {
		return "product/product";
	}

	@Override
	protected PageableDataTableFacade<ProductItem, MinimalProductData> getPageableFacade() {
		return getProductFacade();
	}


}
