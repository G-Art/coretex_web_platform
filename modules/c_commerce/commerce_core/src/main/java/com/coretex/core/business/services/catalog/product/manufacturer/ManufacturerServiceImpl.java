package com.coretex.core.business.services.catalog.product.manufacturer;


import com.coretex.core.business.repositories.catalog.product.manufacturer.ManufacturerDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service("manufacturerService")
public class ManufacturerServiceImpl extends
		SalesManagerEntityServiceImpl<ManufacturerItem> implements ManufacturerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerServiceImpl.class);

	private ManufacturerDao manufacturerDao;

	public ManufacturerServiceImpl(
			ManufacturerDao manufacturerDao) {
		super(manufacturerDao);
		this.manufacturerDao = manufacturerDao;
	}


	@Override
	public Long getCountManufAttachedProducts(ManufacturerItem manufacturer)  {
		return manufacturerDao.countByProduct(manufacturer.getUuid());
		//.getCountManufAttachedProducts( manufacturer );
	}


	@Override
	public List<ManufacturerItem> listByStore(MerchantStoreItem store, LocaleItem language)  {
		return manufacturerDao.findByStoreAndLanguage(store.getUuid(), language.getUuid());
	}

	@Override
	public List<ManufacturerItem> listByStore(MerchantStoreItem store)  {
		return manufacturerDao.findByStore(store.getUuid());
	}

	@Override
	public List<ManufacturerItem> listByProductsByCategoriesId(MerchantStoreItem store, List<UUID> ids, LocaleItem language)  {
		return manufacturerDao.findByCategoriesAndLanguage(ids, language.getUuid());
	}

	@Override
	public void saveOrUpdate(ManufacturerItem manufacturer)  {

		LOGGER.debug("Creating ManufacturerItem");
		super.save(manufacturer);
	}

	@Override
	public ManufacturerItem getByCode(MerchantStoreItem store, String code) {
		return manufacturerDao.findByCodeAndMerchandStore(code, store.getUuid());
	}
}
