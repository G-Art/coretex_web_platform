package com.coretex.core.model.order;

import com.coretex.items.commerce_core_model.OrderTotalItem;

import java.util.List;

/**
 * Contains a list of negative OrderTotalItem variation
 * that will be shown in the order summary
 *
 * @author carlsamson
 */
public abstract class OrderTotalVariation {

	List<OrderTotalItem> variations = null;

	public List<OrderTotalItem> getVariations() {
		return variations;
	}

	public void setVariations(List<OrderTotalItem> variations) {
		this.variations = variations;
	}

}
