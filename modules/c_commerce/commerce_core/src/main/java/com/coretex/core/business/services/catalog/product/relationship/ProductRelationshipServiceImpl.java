package com.coretex.core.business.services.catalog.product.relationship;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.catalog.product.relationship.ProductRelationshipDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.model.catalog.product.relationship.ProductRelationshipType;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productRelationshipService")
public class ProductRelationshipServiceImpl extends
		SalesManagerEntityServiceImpl<ProductRelationshipItem> implements
		ProductRelationshipService {


	private ProductRelationshipDao productRelationshipDao;

	public ProductRelationshipServiceImpl(
			ProductRelationshipDao productRelationshipDao) {
		super(productRelationshipDao);
		this.productRelationshipDao = productRelationshipDao;
	}

	@Override
	public void saveOrUpdate(ProductRelationshipItem relationship) throws ServiceException {

		this.save(relationship);

	}


	@Override
	public void addGroup(MerchantStoreItem store, String groupName) throws ServiceException {
		ProductRelationshipItem relationship = new ProductRelationshipItem();
		relationship.setCode(groupName);
		relationship.setStore(store);
		relationship.setActive(true);
		this.save(relationship);
	}

	@Override
	public List<ProductRelationshipItem> getGroups(MerchantStoreItem store) {
		return productRelationshipDao.getGroups(store);
	}

	@Override
	public void deleteGroup(MerchantStoreItem store, String groupName) throws ServiceException {
		List<ProductRelationshipItem> entities = productRelationshipDao.getByGroup(store, groupName);
		for (ProductRelationshipItem relation : entities) {
			this.delete(relation);
		}
	}

	@Override
	public void deactivateGroup(MerchantStoreItem store, String groupName) throws ServiceException {
		List<ProductRelationshipItem> entities = productRelationshipDao.getByGroup(store, groupName);
		for (ProductRelationshipItem relation : entities) {
			relation.setActive(false);
			this.saveOrUpdate(relation);
		}
	}

	@Override
	public void activateGroup(MerchantStoreItem store, String groupName) throws ServiceException {
		List<ProductRelationshipItem> entities = this.getByGroup(store, groupName);
		for (ProductRelationshipItem relation : entities) {
			relation.setActive(true);
			this.saveOrUpdate(relation);
		}
	}

	public void delete(ProductRelationshipItem relationship) {

		//throws detached exception so need to query first
		relationship = this.getById(relationship.getUuid());
		super.delete(relationship);


	}

	@Override
	public List<ProductRelationshipItem> listByProduct(ProductItem product) {

		return productRelationshipDao.listByProducts(product);

	}


	@Override
	public List<ProductRelationshipItem> getByType(MerchantStoreItem store, ProductItem product, ProductRelationshipType type, LanguageItem language) {

		return productRelationshipDao.getByType(store, type.name(), product, language);

	}

	@Override
	public List<ProductRelationshipItem> getByType(MerchantStoreItem store, ProductRelationshipType type, LanguageItem language) {
		return productRelationshipDao.getByType(store, type.name(), language);
	}

	@Override
	public List<ProductRelationshipItem> getByType(MerchantStoreItem store, ProductRelationshipType type) {

		return productRelationshipDao.getByType(store, type.name());

	}

	@Override
	public List<ProductRelationshipItem> getByGroup(MerchantStoreItem store, String groupName) {

		return productRelationshipDao.getByType(store, groupName);

	}

	@Override
	public List<ProductRelationshipItem> getByGroup(MerchantStoreItem store, String groupName, LanguageItem language) throws ServiceException {

		return productRelationshipDao.getByType(store, groupName, language);

	}

	@Override
	public List<ProductRelationshipItem> getByType(MerchantStoreItem store, ProductItem product, ProductRelationshipType type) throws ServiceException {


		return productRelationshipDao.getByType(store, type.name(), product);


	}


}
