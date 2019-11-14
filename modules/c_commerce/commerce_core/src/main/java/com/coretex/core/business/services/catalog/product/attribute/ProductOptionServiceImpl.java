package com.coretex.core.business.services.catalog.product.attribute;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.ProductOptionItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.catalog.product.attribute.ProductOptionDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.ProductAttributeItem;

@Service("productOptionService")
public class ProductOptionServiceImpl extends
		SalesManagerEntityServiceImpl<ProductOptionItem> implements ProductOptionService {


	private ProductOptionDao productOptionDao;

	@Resource
	private ProductAttributeService productAttributeService;

	public ProductOptionServiceImpl(
			ProductOptionDao productOptionDao) {
		super(productOptionDao);
		this.productOptionDao = productOptionDao;
	}

	@Override
	public List<ProductOptionItem> listByStore(MerchantStoreItem store, LanguageItem language) {
		return productOptionDao.findByStoreId(store.getUuid(), language.getUuid());
	}

	@Override
	public List<ProductOptionItem> listReadOnly(MerchantStoreItem store, LanguageItem language) {
		return productOptionDao.findByReadOnly(store.getUuid(), language.getUuid(), true);
	}


	@Override
	public List<ProductOptionItem> getByName(MerchantStoreItem store, String name, LanguageItem language) throws ServiceException {

		try {
			return productOptionDao.findByName(store.getUuid(), name, language.getUuid());
		} catch (Exception e) {
			throw new ServiceException(e);
		}


	}

	@Override
	public void saveOrUpdate(ProductOptionItem entity) throws ServiceException {
		super.save(entity);
	}

	@Override
	public void delete(ProductOptionItem entity) {

		//remove all attributes having this option
		List<ProductAttributeItem> attributes = productAttributeService.getByOptionId(entity.getMerchantStore(), entity.getUuid());

		for (ProductAttributeItem attribute : attributes) {
			productAttributeService.delete(attribute);
		}

		ProductOptionItem option = this.getById(entity.getUuid());

		//remove option
		super.delete(option);

	}

	@Override
	public ProductOptionItem getByCode(MerchantStoreItem store, String optionCode) {
		return productOptionDao.findByCode(store.getUuid(), optionCode);
	}

	@Override
	public ProductOptionItem getById(MerchantStoreItem store, UUID optionId) {
		return productOptionDao.findOne(store.getUuid(), optionId);
	}


}
