package com.coretex.shop.store.controller.items.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.catalog.product.ReadableProductList;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.utils.ImageFilePath;

@Component
public class ProductItemsFacadeImpl implements ProductItemsFacade {


	@Resource
	ProductService productService;

	@Resource
	PricingService pricingService;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@Resource
	private ProductRelationshipService productRelationshipService;

	@Override
	public ReadableProductList listItemsByManufacturer(MerchantStoreItem store,
													   LocaleItem language, UUID manufacturerId, int startCount, int maxCount) throws Exception {


		ProductCriteria productCriteria = new ProductCriteria();
		productCriteria.setMaxCount(maxCount);
		productCriteria.setStartIndex(startCount);


		productCriteria.setManufacturerId(manufacturerId);
		com.coretex.core.model.catalog.product.ProductList products = productService.listByStore(store, language, productCriteria);


		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);


		ReadableProductList productList = new ReadableProductList();
		for (ProductItem product : products.getProducts()) {

			//create new proxy product
			ReadableProduct readProduct = populator.populate(product, new ReadableProduct(), store, language);
			productList.getProducts().add(readProduct);

		}

		productList.setTotalCount(products.getTotalCount());


		return productList;
	}

	@Override
	public ReadableProductList listItemsByIds(MerchantStoreItem store, LocaleItem language, List<UUID> ids, int startCount,
											  int maxCount) throws Exception {

		if (CollectionUtils.isEmpty(ids)) {
			return new ReadableProductList();
		}


		ProductCriteria productCriteria = new ProductCriteria();
		productCriteria.setMaxCount(maxCount);
		productCriteria.setStartIndex(startCount);
		productCriteria.setProductIds(ids);


		com.coretex.core.model.catalog.product.ProductList products = productService.listByStore(store, language, productCriteria);


		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);


		ReadableProductList productList = new ReadableProductList();
		for (ProductItem product : products.getProducts()) {

			//create new proxy product
			ReadableProduct readProduct = populator.populate(product, new ReadableProduct(), store, language);
			productList.getProducts().add(readProduct);

		}

		productList.setTotalCount(products.getTotalCount());


		return productList;
	}

	@Override
	public ReadableProductList listItemsByGroup(String group, MerchantStoreItem store, LocaleItem language) throws Exception {


		//get product group
		List<ProductRelationshipItem> groups = productRelationshipService.getByGroup(store, group, language);

		if (group != null) {
			List<UUID> ids = new ArrayList<>();
			for (ProductRelationshipItem relationship : groups) {
				ProductItem product = relationship.getRelatedProduct();
				ids.add(product.getUuid());
			}

			ReadableProductList list = listItemsByIds(store, language, ids, 0, 0);
			return list;
		}

		return null;
	}

	@Override
	public ReadableProductList addItemToGroup(ProductItem product, String group, MerchantStoreItem store, LocaleItem language)
			throws Exception {

		Validate.notNull(product, "ProductItem muust not be null");
		Validate.notNull(group, "group must not be null");

		ProductRelationshipItem relationship = new ProductRelationshipItem();
		relationship.setActive(true);
		relationship.setCode(group);
		relationship.setStore(store);
		relationship.setRelatedProduct(product);

		productRelationshipService.saveOrUpdate(relationship);


		return listItemsByGroup(group, store, language);
	}

	@Override
	public ReadableProductList removeItemFromGroup(ProductItem product, String group, MerchantStoreItem store,
												   LocaleItem language) throws Exception {

		ProductRelationshipItem relationship = null;
		List<ProductRelationshipItem> relationships = productRelationshipService.getByGroup(store, group);

		for (ProductRelationshipItem r : relationships) {
			if (r.getRelatedProduct().getUuid().equals(product.getUuid())) {
				relationship = r;
				break;
			}
		}

		productRelationshipService.delete(relationship);

		return listItemsByGroup(group, store, language);
	}

}
