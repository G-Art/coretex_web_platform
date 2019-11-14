package com.coretex.shop.populator.customer;


import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.items.commerce_core_model.CustomerOptionSetItem;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.admin.model.customer.attribute.CustomerOption;
import com.coretex.shop.admin.model.customer.attribute.CustomerOptionValue;


import java.util.ArrayList;
import java.util.List;


/**
 * Used in the admin section
 *
 * @author c.samson
 */

public class CustomerOptionPopulator extends
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
			values = new ArrayList<>();
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
