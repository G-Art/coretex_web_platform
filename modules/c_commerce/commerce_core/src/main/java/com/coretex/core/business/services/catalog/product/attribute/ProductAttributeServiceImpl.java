package com.coretex.core.business.services.catalog.product.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.stereotype.Service;


import com.coretex.core.business.repositories.catalog.product.attribute.ProductAttributeDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.core.LocaleItem;

@Service("productAttributeService")
public class ProductAttributeServiceImpl extends
		SalesManagerEntityServiceImpl<ProductAttributeItem> implements ProductAttributeService {

	private ProductAttributeDao productAttributeDao;

	public ProductAttributeServiceImpl(ProductAttributeDao productAttributeDao) {
		super(productAttributeDao);
		this.productAttributeDao = productAttributeDao;
	}


	@Override
	public List<ProductAttributeItem> getByOptionId(MerchantStoreItem store,
													UUID id) {

		return productAttributeDao.findByOptionId(store.getUuid(), id);

	}

	@Override
	public List<ProductAttributeItem> getByAttributeIds(MerchantStoreItem store,
														ProductItem product, List<UUID> ids) {

		return productAttributeDao.findByAttributeIds(store.getUuid(), product.getUuid(), ids);

	}

	@Override
	public List<ProductAttributeItem> getByOptionValueId(MerchantStoreItem store,
														 UUID id) {

		return productAttributeDao.findByOptionValueId(store.getUuid(), id);

	}

	/**
	 * Returns all product attributes
	 */
	@Override
	public List<ProductAttributeItem> getByProductId(MerchantStoreItem store,
													 ProductItem product, LocaleItem language) {
		return productAttributeDao.findByProductId(store.getUuid(), product.getUuid(), language.getUuid());

	}


	@Override
	public void saveOrUpdate(ProductAttributeItem productAttribute)
			 {
		//if(productAttribute.getUuid()!=null && productAttribute.getUuid()>0) {
		//	productAttributeDao.update(productAttribute);
		//} else {
		productAttributeDao.save(productAttribute);
		//}

	}

	@Override
	public void delete(ProductAttributeItem attribute) {

		//override method, this allows the error that we try to remove a detached instance
		attribute = this.getByUUID(attribute.getUuid());
		super.delete(attribute);

	}

}
