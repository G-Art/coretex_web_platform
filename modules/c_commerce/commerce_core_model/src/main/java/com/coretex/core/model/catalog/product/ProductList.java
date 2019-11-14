package com.coretex.core.model.catalog.product;

import java.util.ArrayList;
import java.util.List;

import com.coretex.core.model.common.EntityList;
import com.coretex.items.commerce_core_model.ProductItem;

public class ProductList extends EntityList {



	private static final long serialVersionUID = 7267292601646149482L;
	private List<ProductItem> products = new ArrayList<ProductItem>();

	public List<ProductItem> getProducts() {
		return products;
	}

	public void setProducts(List<ProductItem> products) {
		this.products = products;
	}


}
