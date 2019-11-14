package com.coretex.core.business.services.catalog.product.attribute;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.ProductOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.catalog.product.attribute.ProductOptionValueDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;

@Service("productOptionValueService")
public class ProductOptionValueServiceImpl extends
		SalesManagerEntityServiceImpl<ProductOptionValueItem> implements
		ProductOptionValueService {

	@Resource
	private ProductAttributeService productAttributeService;

	private ProductOptionValueDao productOptionValueDao;

	public ProductOptionValueServiceImpl(
			ProductOptionValueDao productOptionValueDao) {
		super(productOptionValueDao);
		this.productOptionValueDao = productOptionValueDao;
	}


	@Override
	public List<ProductOptionValueItem> listByStore(MerchantStoreItem store, LanguageItem language) throws ServiceException {

		return productOptionValueDao.findByStoreId(store.getUuid(), language.getUuid());
	}

	@Override
	public List<ProductOptionValueItem> listByStoreNoReadOnly(MerchantStoreItem store, LanguageItem language) {

		return productOptionValueDao.findByReadOnly(store.getUuid(), language.getUuid(), false);
	}

	@Override
	public List<ProductOptionValueItem> getByName(MerchantStoreItem store, String name, LanguageItem language) throws ServiceException {

		try {
			return productOptionValueDao.findByName(store.getUuid(), name, language.getUuid());
		} catch (Exception e) {
			throw new ServiceException(e);
		}


	}

	@Override
	public void saveOrUpdate(ProductOptionValueItem entity) throws ServiceException {


		super.save(entity);

	}


	public void delete(ProductOptionValueItem entity) {

		//remove all attributes having this option
		List<ProductAttributeItem> attributes = productAttributeService.getByOptionValueId(entity.getMerchantStore(), entity.getUuid());

		for (ProductAttributeItem attribute : attributes) {
			productAttributeService.delete(attribute);
		}

		ProductOptionValueItem option = getById(entity.getUuid());

		//remove option
		super.delete(option);

	}

	@Override
	public ProductOptionValueItem getByCode(MerchantStoreItem store, String optionValueCode) {
		return productOptionValueDao.findByCode(store.getUuid(), optionValueCode);
	}


	@Override
	public ProductOptionValueItem getById(MerchantStoreItem store, UUID optionValueId) {
		return productOptionValueDao.findOne(store.getUuid(), optionValueId);
	}


}
