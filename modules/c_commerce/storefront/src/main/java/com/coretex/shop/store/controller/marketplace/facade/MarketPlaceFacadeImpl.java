package com.coretex.shop.store.controller.marketplace.facade;

import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.marketplace.ReadableMarketPlace;
import com.coretex.shop.model.shop.ReadableMerchantStore;
import com.coretex.shop.store.controller.store.facade.StoreFacade;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MarketPlaceFacadeImpl implements MarketPlaceFacade {

	@Resource
	private StoreFacade storeFacade;


	@Override
	public ReadableMarketPlace get(String store, LocaleItem lang) {
		ReadableMerchantStore readableStore = storeFacade.getByCode(store, lang);
		return createReadableMarketPlace(readableStore);
	}

	private ReadableMarketPlace createReadableMarketPlace(ReadableMerchantStore readableStore) {
		//TODO add info from Entity
		ReadableMarketPlace marketPlace = new ReadableMarketPlace();
		marketPlace.setStore(readableStore);
		return marketPlace;
	}

}
