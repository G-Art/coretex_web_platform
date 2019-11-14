package com.coretex.shop.model.shoppingcart;

import java.io.Serializable;
import java.util.UUID;

import com.coretex.shop.model.entity.ShopEntity;

public class ShoppingCartAttribute extends ShopEntity implements Serializable {



	private static final long serialVersionUID = 1L;
	private UUID optionId;
	private UUID optionValueId;
	private UUID attributeId;
	private String optionName;
	private String optionValue;

	public UUID getOptionId() {
		return optionId;
	}

	public void setOptionId(UUID optionId) {
		this.optionId = optionId;
	}

	public UUID getOptionValueId() {
		return optionValueId;
	}

	public void setOptionValueId(UUID optionValueId) {
		this.optionValueId = optionValueId;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public UUID getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(UUID attributeId) {
		this.attributeId = attributeId;
	}

}
