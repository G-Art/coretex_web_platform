package com.coretex.shop.store.controller.marketplace.facade;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OptinItem;
import com.coretex.shop.store.api.exception.ConversionRuntimeException;
import com.coretex.shop.store.api.exception.ResourceNotFoundException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;

import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.coretex.core.business.services.system.optin.OptinService;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.enums.commerce_core_model.OptinTypeEnum;
import com.coretex.shop.model.marketplace.ReadableMarketPlace;
import com.coretex.shop.model.shop.ReadableMerchantStore;
import com.coretex.shop.model.system.ReadableOptin;
import com.coretex.shop.populator.system.ReadableOptinPopulator;
import com.coretex.shop.store.controller.store.facade.StoreFacade;

@Component
public class MarketPlaceFacadeImpl implements MarketPlaceFacade {

	@Resource
	private StoreFacade storeFacade;

	@Resource
	private OptinService optinService;

	@Override
	public ReadableMarketPlace get(String store, LanguageItem lang) {
		ReadableMerchantStore readableStore = storeFacade.getByCode(store, lang);
		return createReadableMarketPlace(readableStore);
	}

	private ReadableMarketPlace createReadableMarketPlace(ReadableMerchantStore readableStore) {
		//TODO add info from Entity
		ReadableMarketPlace marketPlace = new ReadableMarketPlace();
		marketPlace.setStore(readableStore);
		return marketPlace;
	}

	@Override
	public ReadableOptin findByMerchantAndType(MerchantStoreItem store, OptinTypeEnum type) {
		OptinItem optin = getOptinByMerchantAndType(store, type);
		return convertOptinToReadableOptin(store, optin);
	}

	private OptinItem getOptinByMerchantAndType(MerchantStoreItem store, OptinTypeEnum type) {
		try {
			return Optional.ofNullable(optinService.getOptinByMerchantAndType(store, type))
					.orElseThrow(() -> new ResourceNotFoundException("Option not found"));
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}

	}

	private ReadableOptin convertOptinToReadableOptin(MerchantStoreItem store, OptinItem optin) {
		try {
			ReadableOptinPopulator populator = new ReadableOptinPopulator();
			return populator.populate(optin, null, store, null);
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}

	}

}
