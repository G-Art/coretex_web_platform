package com.coretex.shop.model.catalog.product.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReadableProductVariant implements Serializable {


	private static final long serialVersionUID = 1L;
	private List<ReadableProductVariantValue> options = new ArrayList<ReadableProductVariantValue>();

	public List<ReadableProductVariantValue> getOptions() {
		return options;
	}

	public void setOptions(List<ReadableProductVariantValue> options) {
		this.options = options;
	}


}
