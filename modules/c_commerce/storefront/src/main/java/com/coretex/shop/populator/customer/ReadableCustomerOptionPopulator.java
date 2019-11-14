package com.coretex.shop.populator.customer;

import java.util.ArrayList;
import java.util.List;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.items.commerce_core_model.CustomerOptionSetItem;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.admin.model.customer.attribute.CustomerOption;
import com.coretex.shop.admin.model.customer.attribute.CustomerOptionValue;


public class ReadableCustomerOptionPopulator extends
		AbstractDataPopulator<CustomerOptionItem, com.coretex.shop.admin.model.customer.attribute.CustomerOption> {


	private CustomerOptionSetItem optionSet;

	public CustomerOptionSetItem getOptionSet() {
		return optionSet;
	}

	public void setOptionSet(CustomerOptionSetItem optionSet) {
		this.optionSet = optionSet;
	}


	@Override
	public CustomerOption populate(
			CustomerOptionItem source,
			CustomerOption target, MerchantStoreItem store, LanguageItem language) throws ConversionException {


		CustomerOption customerOption = target;
		if (customerOption == null) {
			customerOption = new CustomerOption();
		}

		customerOption.setUuid(source.getUuid());
		customerOption.setType(source.getCustomerOptionType());
		customerOption.setName(source.getName());

		List<CustomerOptionValue> values = customerOption.getAvailableValues();
		if (values == null) {
			values = new ArrayList<CustomerOptionValue>();
			customerOption.setAvailableValues(values);
		}

		CustomerOptionValueItem optionValue = optionSet.getCustomerOptionValue();
		CustomerOptionValue custOptValue = new CustomerOptionValue();
		custOptValue.setUuid(optionValue.getUuid());
		custOptValue.setLanguage(language.getCode());
		custOptValue.setName(optionValue.getName());
		values.add(custOptValue);

		return customerOption;

	}

	@Override
	protected CustomerOption createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
