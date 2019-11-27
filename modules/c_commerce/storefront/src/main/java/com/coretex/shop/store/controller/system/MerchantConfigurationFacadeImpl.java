package com.coretex.shop.store.controller.system;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.system.MerchantConfigurationService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.core.model.system.MerchantConfig;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;
import com.coretex.shop.model.system.Configs;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;

import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import static com.coretex.shop.constants.Constants.KEY_FACEBOOK_PAGE_URL;
import static com.coretex.shop.constants.Constants.KEY_GOOGLE_ANALYTICS_URL;
import static com.coretex.shop.constants.Constants.KEY_INSTAGRAM_URL;
import static com.coretex.shop.constants.Constants.KEY_PINTEREST_PAGE_URL;

@Service
public class MerchantConfigurationFacadeImpl implements MerchantConfigurationFacade {

	@Resource
	private MerchantConfigurationService merchantConfigurationService;

	@Override
	public Configs getMerchantConfig(MerchantStoreItem merchantStore, LocaleItem language) {

		MerchantConfig configs = getMerchantConfig(merchantStore);

		Configs readableConfig = new Configs();
		readableConfig.setAllowOnlinePurchase(configs.isAllowPurchaseItems());
		readableConfig.setDisplaySearchBox(configs.isDisplaySearchBox());
		readableConfig.setDisplayContactUs(configs.isDisplayContactUs());

		Optional<String> facebookConfigValue = getConfigValue(KEY_FACEBOOK_PAGE_URL, merchantStore);
		facebookConfigValue.ifPresent(readableConfig::setFacebook);

		Optional<String> googleConfigValue = getConfigValue(KEY_GOOGLE_ANALYTICS_URL, merchantStore);
		googleConfigValue.ifPresent(readableConfig::setGa);

		Optional<String> instagramConfigValue = getConfigValue(KEY_INSTAGRAM_URL, merchantStore);
		instagramConfigValue.ifPresent(readableConfig::setInstagram);


		Optional<String> pinterestConfigValue = getConfigValue(KEY_PINTEREST_PAGE_URL, merchantStore);
		pinterestConfigValue.ifPresent(readableConfig::setPinterest);

		return readableConfig;
	}

	private MerchantConfig getMerchantConfig(MerchantStoreItem merchantStore) {
		try {
			return merchantConfigurationService.getMerchantConfig(merchantStore);
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	private Optional<String> getConfigValue(String keyContant, MerchantStoreItem merchantStore) {
		return getMerchantConfiguration(keyContant, merchantStore)
				.map(MerchantConfigurationItem::getValue);
	}

	private Optional<MerchantConfigurationItem> getMerchantConfiguration(String key, MerchantStoreItem merchantStore) {
		try {
			return Optional.ofNullable(merchantConfigurationService.getMerchantConfiguration(key, merchantStore));
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}

	}
}
