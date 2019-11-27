package com.coretex.shop.store.controller.store.facade;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.coretex.core.business.constants.Constants;
import com.coretex.enums.commerce_core_model.MerchantConfigurationTypeEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.content.ContentService;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.system.MerchantConfigurationService;
import com.coretex.core.constants.MeasureUnit;
import com.coretex.core.model.common.GenericEntityList;
import com.coretex.core.model.content.InputContentFile;
import com.coretex.core.model.merchant.MerchantStoreCriteria;
import com.coretex.shop.model.content.ReadableImage;
import com.coretex.shop.model.shop.MerchantConfigEntity;
import com.coretex.shop.model.shop.PersistableBrand;
import com.coretex.shop.model.shop.PersistableMerchantStore;
import com.coretex.shop.model.shop.ReadableBrand;
import com.coretex.shop.model.shop.ReadableMerchantStore;
import com.coretex.shop.model.shop.ReadableMerchantStoreList;
import com.coretex.shop.populator.store.PersistableMerchantStorePopulator;
import com.coretex.shop.populator.store.ReadableMerchantStorePopulator;
import com.coretex.shop.store.api.exception.ConversionRuntimeException;
import com.coretex.shop.store.api.exception.ResourceNotFoundException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;
import com.coretex.shop.utils.ImageFilePath;
import com.coretex.shop.utils.LanguageUtils;

@Service("storeFacade")
public class StoreFacadeImpl implements StoreFacade {

	@Resource
	private MerchantStoreService merchantStoreService;

	@Resource
	private MerchantConfigurationService merchantConfigurationService;

	@Resource
	private LanguageService languageService;

	@Resource
	private CountryService countryService;

	@Resource
	private ZoneService zoneService;

	@Resource
	private ContentService contentService;

	@Resource
	private PersistableMerchantStorePopulator persistableMerchantStorePopulator;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@Resource
	private LanguageUtils languageUtils;

	private static final Logger LOG = LoggerFactory.getLogger(StoreFacadeImpl.class);

	@Override
	public MerchantStoreItem getByCode(HttpServletRequest request) {
		String code = request.getParameter("store");
		if (StringUtils.isEmpty(code)) {
			code = com.coretex.core.business.constants.Constants.DEFAULT_STORE;
		}
		return get(code);
	}

	@Override
	public MerchantStoreItem get(String code) {
		return merchantStoreService.getByCode(code);
	}

	@Override
	public ReadableMerchantStore getByCode(String code, String lang) {
		LocaleItem language = getLanguage(lang);
		return getByCode(code, language);
	}

	private LocaleItem getLanguage(String lang) {
		return languageUtils.getServiceLanguage(lang);
	}

	@Override
	public ReadableMerchantStore getByCode(String code, LocaleItem language) {
		MerchantStoreItem store = getMerchantStoreByCode(code);
		return convertMerchantStoreToReadableMerchantStore(language, store);
	}

	@Override
	public boolean existByCode(String code) {
		return merchantStoreService.getByCode(code) != null;
	}

	private ReadableMerchantStore convertMerchantStoreToReadableMerchantStore(LocaleItem language,
																			  MerchantStoreItem store) {
		ReadableMerchantStore readable = new ReadableMerchantStore();

		ReadableMerchantStorePopulator populator = new ReadableMerchantStorePopulator();
		populator.setCountryService(countryService);
		populator.setZoneService(zoneService);
		populator.setFilePath(imageUtils);

		/**
		 * LocaleItem is not important for this conversion using default language
		 */
		try {
			readable = populator.populate(store, readable, store, language);
		} catch (Exception e) {
			throw new ConversionRuntimeException(
					"Error while populating MerchantStoreItem " + e.getMessage());
		}
		return readable;
	}

	private MerchantStoreItem getMerchantStoreByCode(String code) {
		return Optional.ofNullable(get(code))
				.orElseThrow(() -> new ResourceNotFoundException("Merchant store code not found"));
	}

	@Override
	public ReadableMerchantStore create(PersistableMerchantStore store) {

		Validate.notNull(store, "PersistableMerchantStore must not be null");
		Validate.notNull(store.getCode(), "PersistableMerchantStore.code must not be null");

		// check if store code exists
		MerchantStoreItem storeForCheck = get(store.getCode());
		if (storeForCheck != null) {
			throw new ServiceRuntimeException("MerhantStore " + store.getCode() + " already exists");
		}

		MerchantStoreItem mStore =
				convertPersistableMerchantStoreToMerchantStore(store, languageService.defaultLanguage());
		createMerchantStore(mStore);

		ReadableMerchantStore storeTO = getByCode(store.getCode(), languageService.defaultLanguage());
		return storeTO;
	}

	private void createMerchantStore(MerchantStoreItem mStore) {
		merchantStoreService.create(mStore);
	}

	private MerchantStoreItem convertPersistableMerchantStoreToMerchantStore(
			PersistableMerchantStore store, LocaleItem language) {
		MerchantStoreItem mStore = new MerchantStoreItem();

		// set default values
		mStore.setWeightUnitCode(MeasureUnit.KG.name());
		mStore.setSeizeUnitCode(MeasureUnit.IN.name());

		try {
			mStore = persistableMerchantStorePopulator.populate(store, mStore, language);
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}
		return mStore;
	}

	@Override
	public ReadableMerchantStore update(PersistableMerchantStore store) {

		Validate.notNull(store);

		MerchantStoreItem mStore = mergePersistableMerchantStoreToMerchantStore(store, store.getCode(),
				languageService.defaultLanguage());

		updateMerchantStore(mStore);

		ReadableMerchantStore storeTO = getByCode(store.getCode(), languageService.defaultLanguage());
		return storeTO;
	}

	private void updateMerchantStore(MerchantStoreItem mStore) {
		merchantStoreService.update(mStore);
	}

	private MerchantStoreItem mergePersistableMerchantStoreToMerchantStore(PersistableMerchantStore store,
																		   String code, LocaleItem language) {

		MerchantStoreItem mStore = getMerchantStoreByCode(code);

		store.setId(mStore.getUuid());

		try {
			mStore = persistableMerchantStorePopulator.populate(store, mStore, language);
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}
		return mStore;
	}

	@Override
	public ReadableMerchantStoreList getByCriteria(MerchantStoreCriteria criteria, String drawParam,
												   LocaleItem lang) {
		GenericEntityList<MerchantStoreItem> entityList = getMerchantStoresByCriteria(criteria);

		List<MerchantStoreItem> stores = entityList.getList();

		return createReadableMerchantStoreList(drawParam, lang, entityList, stores);
	}

	private ReadableMerchantStoreList createReadableMerchantStoreList(String drawParam, LocaleItem lang,
																	  GenericEntityList<MerchantStoreItem> list, List<MerchantStoreItem> stores) {
		ReadableMerchantStoreList merchantStoreToList = new ReadableMerchantStoreList();
		merchantStoreToList.setTotalCount(list.getTotalCount());

		List<ReadableMerchantStore> readableMerchantStores =
				stores.stream().map(store -> convertMerchantStoreToReadableMerchantStore(lang, store))
						.collect(Collectors.toList());

		merchantStoreToList.getData().addAll(readableMerchantStores);

		merchantStoreToList.setRecordsFiltered(merchantStoreToList.getTotalCount());
		merchantStoreToList.setRecordsTotal(merchantStoreToList.getTotalCount());

		if (!org.apache.commons.lang3.StringUtils.isEmpty(drawParam)) {
			merchantStoreToList.setDraw(Integer.parseInt(drawParam));
		}
		return merchantStoreToList;
	}

	private GenericEntityList<MerchantStoreItem> getMerchantStoresByCriteria(
			MerchantStoreCriteria criteria) {
		try {
			return Optional.ofNullable(merchantStoreService.getByCriteria(criteria))
					.orElseThrow(() -> new ResourceNotFoundException("Criteria did not match any store"));
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}

	}

	@Override
	public void delete(String code) {

		if (Constants.DEFAULT_STORE.equals(code.toUpperCase())) {
			throw new ServiceRuntimeException("Cannot remove default store");
		}

		MerchantStoreItem mStore = getMerchantStoreByCode(code);

		try {
			merchantStoreService.delete(mStore);
		} catch (Exception e) {
			LOG.error("Error while deleting MerchantStoreItem", e);
			throw new ServiceRuntimeException("Error while deleting MerchantStoreItem " + e.getMessage());
		}

	}

	@Override
	public ReadableBrand getBrand(String code) {
		MerchantStoreItem mStore = getMerchantStoreByCode(code);

		ReadableBrand readableBrand = new ReadableBrand();
		if (!StringUtils.isEmpty(mStore.getStoreLogo())) {
			String imagePath = imageUtils.buildStoreLogoFilePath(mStore);
			ReadableImage image = createReadableImage(mStore.getStoreLogo(), imagePath);
			readableBrand.setLogo(image);
		}
		List<MerchantConfigEntity> merchantConfigTOs = getMerchantConfigEntities(mStore);
		readableBrand.getSocialNetworks().addAll(merchantConfigTOs);
		return readableBrand;
	}

	private List<MerchantConfigEntity> getMerchantConfigEntities(MerchantStoreItem mStore) {
		List<MerchantConfigurationItem> configurations =
				getMergeConfigurationsByStore(MerchantConfigurationTypeEnum.SOCIAL, mStore);

		return configurations.stream().map(config -> convertToMerchantConfigEntity(config))
				.collect(Collectors.toList());
	}

	private List<MerchantConfigurationItem> getMergeConfigurationsByStore(
			MerchantConfigurationTypeEnum configurationType, MerchantStoreItem mStore) {
		try {
			return merchantConfigurationService.listByType(configurationType, mStore);
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(
					"Error wile getting merchantConfigurations " + e.getMessage());
		}
	}

	private MerchantConfigEntity convertToMerchantConfigEntity(MerchantConfigurationItem config) {
		MerchantConfigEntity configTO = new MerchantConfigEntity();
		configTO.setUuid(config.getUuid());
		configTO.setKey(config.getKey());
		configTO.setType(config.getMerchantConfigurationType());
		configTO.setValue(config.getValue());
		configTO.setActive(config.getActive() != null ? config.getActive() : false);
		return configTO;
	}

	private MerchantConfigurationItem convertToMerchantConfiguration(MerchantConfigEntity config,
																	 MerchantConfigurationTypeEnum configurationType) {
		MerchantConfigurationItem configTO = new MerchantConfigurationItem();
		configTO.setUuid(config.getUuid());
		configTO.setKey(config.getKey());
		configTO.setMerchantConfigurationType(configurationType);
		configTO.setValue(config.getValue());
		configTO.setActive(new Boolean(config.isActive()));
		return configTO;
	}

	private ReadableImage createReadableImage(String storeLogo, String imagePath) {
		ReadableImage image = new ReadableImage();
		image.setName(storeLogo);
		image.setPath(imagePath);
		return image;
	}

	@Override
	public void deleteLogo(String code) {
		MerchantStoreItem store = getByCode(code);
		String image = store.getStoreLogo();
		store.setStoreLogo(null);

		try {
			updateMerchantStore(store);
			if (!StringUtils.isEmpty(image)) {
				contentService.removeFile(store.getCode(), image);
			}
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e.getMessage());
		}
	}

	@Override
	public MerchantStoreItem getByCode(String code) {
		return getMerchantStoreByCode(code);
	}

	@Override
	public void addStoreLogo(String code, InputContentFile cmsContentImage) {
		MerchantStoreItem store = getByCode(code);
		store.setStoreLogo(cmsContentImage.getFileName());
		saveMerchantStore(store);
		addLogoToStore(code, cmsContentImage);
	}

	private void addLogoToStore(String code, InputContentFile cmsContentImage) {
		try {
			contentService.addLogo(code, cmsContentImage);
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	private void saveMerchantStore(MerchantStoreItem store) {
		merchantStoreService.save(store);
	}

	@Override
	public void createBrand(String merchantStoreCode, PersistableBrand brand) {
		MerchantStoreItem mStore = getMerchantStoreByCode(merchantStoreCode);

		List<MerchantConfigEntity> createdConfigs = brand.getSocialNetworks();

		List<MerchantConfigurationItem> configurations = createdConfigs.stream()
				.map(config -> convertToMerchantConfiguration(config, MerchantConfigurationTypeEnum.SOCIAL))
				.collect(Collectors.toList());
		try {
			for (MerchantConfigurationItem mConfigs : configurations) {
				mConfigs.setMerchantStore(mStore);
				if (!StringUtils.isEmpty(mConfigs.getValue())) {
					mConfigs.setMerchantConfigurationType(MerchantConfigurationTypeEnum.SOCIAL);
					merchantConfigurationService.saveOrUpdate(mConfigs);
				} else {// remove if submited blank and exists
					MerchantConfigurationItem config =
							merchantConfigurationService.getMerchantConfiguration(mConfigs.getKey(), mStore);
					if (config != null) {
						merchantConfigurationService.delete(config);
					}
				}
			}
		} catch (ServiceException se) {
			throw new ServiceRuntimeException(se);
		}

	}

}
