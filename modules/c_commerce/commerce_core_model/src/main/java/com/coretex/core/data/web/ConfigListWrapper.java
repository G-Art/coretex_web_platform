package com.coretex.core.data.web;

import java.util.List;

import com.coretex.items.commerce_core_model.MerchantConfigurationItem;


public class ConfigListWrapper {
	private List<MerchantConfigurationItem> merchantConfigs;

	public List<MerchantConfigurationItem> getMerchantConfigs() {
		return merchantConfigs;
	}

	public void setMerchantConfigs(List<MerchantConfigurationItem> merchantConfigs) {
		this.merchantConfigs = merchantConfigs;
	}

}
