package com.coretex.shop.store.controller.optin;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.OptinItem;
import com.coretex.enums.commerce_core_model.OptinTypeEnum;
import com.coretex.shop.model.system.ReadableOptin;
import org.springframework.stereotype.Component;

@Component
public class OptionConverterImpl implements OptinConverter<ReadableOptin, OptinItem> {

	@Override
	public OptinItem convertTo(ReadableOptin source, MerchantStoreItem merchantStore, LanguageItem language) {
		OptinItem optinEntity = new OptinItem();
		optinEntity.setCode(source.getCode());
		optinEntity.setDescription(source.getDescription());
		optinEntity.setOptinType(OptinTypeEnum.valueOf(source.getOptinType()));
		optinEntity.setMerchant(merchantStore);
		return optinEntity;
	}

	@Override
	public ReadableOptin convertFrom(OptinItem source, MerchantStoreItem merchantStore, LanguageItem language) {
		ReadableOptin readable = new ReadableOptin();
		readable.setCode(source.getCode());
		readable.setDescription(source.getDescription());
		readable.setOptinType(source.getOptinType().toString());
		return readable;
	}
}
