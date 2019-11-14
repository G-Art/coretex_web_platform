package com.coretex.shop.store.controller.optin;

import com.coretex.core.business.services.system.optin.OptinService;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OptinItem;
import com.coretex.shop.model.system.ReadableOptin;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OptinFacadeImpl implements OptinFacade {

	@Resource
	private OptinService optinService;

	@Resource
	private OptinConverter<ReadableOptin, OptinItem> converter;

	@Override
	public ReadableOptin create(
			ReadableOptin readableOptin, MerchantStoreItem merchantStore, LanguageItem language) {
		OptinItem optinEntity = converter.convertTo(readableOptin, merchantStore, language);
		OptinItem savedOptinEntity = createOption(optinEntity);
		return converter.convertFrom(savedOptinEntity, merchantStore, language);
	}

	private OptinItem createOption(OptinItem optinEntity) {
		optinService.create(optinEntity);
		return optinEntity;
	}
}
