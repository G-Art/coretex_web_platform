package com.coretex.shop.store.controller.product.facade;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.catalog.manufacturer.PersistableManufacturer;
import com.coretex.shop.model.catalog.manufacturer.ReadableManufacturer;
import com.coretex.shop.model.catalog.product.PersistableProduct;
import com.coretex.shop.model.catalog.product.PersistableProductReview;
import com.coretex.shop.model.catalog.product.ProductPriceEntity;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.catalog.product.ReadableProductList;
import com.coretex.shop.model.catalog.product.ReadableProductReview;

public interface ProductFacade {

	PersistableProduct saveProduct(MerchantStoreItem store, PersistableProduct product, LanguageItem language) throws Exception;

	/**
	 * Get a ProductItem by id and store
	 *
	 * @param store
	 * @param id
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct getProduct(MerchantStoreItem store, UUID id, LanguageItem language) throws Exception;


	/**
	 * Reads a product by code
	 *
	 * @param store
	 * @param uniqueCode
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct getProductByCode(MerchantStoreItem store, String uniqueCode, LanguageItem language) throws Exception;

	/**
	 * Get a product by sku and store
	 *
	 * @param store
	 * @param sku
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct getProduct(MerchantStoreItem store, String sku, LanguageItem language) throws Exception;

	/**
	 * Sets a new price to an existing product
	 *
	 * @param product
	 * @param price
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct updateProductPrice(ReadableProduct product, ProductPriceEntity price, LanguageItem language) throws Exception;

	/**
	 * Sets a new price to an existing product
	 *
	 * @param product
	 * @param quantity
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct updateProductQuantity(ReadableProduct product, int quantity, LanguageItem language) throws Exception;

	/**
	 * Deletes a product for a given product id
	 *
	 * @param product
	 * @throws Exception
	 */
	void deleteProduct(ProductItem product) throws Exception;


	/**
	 * Filters a list of product based on criteria
	 *
	 * @param store
	 * @param language
	 * @param criterias
	 * @return
	 * @throws Exception
	 */
	ReadableProductList getProductListsByCriterias(MerchantStoreItem store, LanguageItem language, ProductCriteria criterias) throws Exception;


	/**
	 * Adds a product to a category
	 *
	 * @param category
	 * @param product
	 * @return
	 * @throws Exception
	 */
	ReadableProduct addProductToCategory(CategoryItem category, ProductItem product, LanguageItem language) throws Exception;

	/**
	 * Removes item from a category
	 *
	 * @param category
	 * @param product
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct removeProductFromCategory(CategoryItem category, ProductItem product, LanguageItem language) throws Exception;


	/**
	 * Saves or updates a ProductItem review
	 *
	 * @param review
	 * @param language
	 * @throws Exception
	 */
	void saveOrUpdateReview(PersistableProductReview review, MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Deletes a product review
	 *
	 * @param review
	 * @param store
	 * @param language
	 * @throws Exception
	 */
	void deleteReview(ProductReviewItem review, MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Get reviews for a given product
	 *
	 * @param product
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	List<ReadableProductReview> getProductReviews(ProductItem product, MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Creates or saves a manufacturer
	 *
	 * @param manufacturer
	 * @param store
	 * @param language
	 * @throws Exception
	 */
	void saveOrUpdateManufacturer(PersistableManufacturer manufacturer, MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Deletes a manufacturer
	 *
	 * @param manufacturer
	 * @param store
	 * @param language
	 * @throws Exception
	 */
	void deleteManufacturer(ManufacturerItem manufacturer, MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Get a ManufacturerItem by id
	 *
	 * @param id
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableManufacturer getManufacturer(UUID id, MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Get all ManufacturerItem
	 *
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	List<ReadableManufacturer> getAllManufacturers(MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Get related items
	 *
	 * @param store
	 * @param product
	 * @param language
	 * @return
	 * @throws Exception
	 */
	List<ReadableProduct> relatedItems(MerchantStoreItem store, ProductItem product, LanguageItem language) throws Exception;

}
