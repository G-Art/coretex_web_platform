package com.coretex.shop.store.controller.system;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.system.Configs;

public interface MerchantConfigurationFacade {

	Configs getMerchantConfig(MerchantStoreItem merchantStore, LanguageItem language);

}
