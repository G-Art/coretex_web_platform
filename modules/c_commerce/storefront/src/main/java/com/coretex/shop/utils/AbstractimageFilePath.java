package com.coretex.shop.utils;

import com.coretex.core.model.content.FileContentType;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.shop.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Properties;


public abstract class AbstractimageFilePath implements ImageFilePath {


	public abstract String getBasePath();

	public abstract void setBasePath(String basePath);

	protected static final String CONTEXT_PATH = "CONTEXT_PATH";

	public @Resource(name = "goodmood-properties")
	Properties properties = new Properties();//shopizer-properties


	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticImageUtils(MerchantStoreItem store, String imageName) {
		StringBuilder imgName = new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).append(FileContentType.IMAGE.name()).append(Constants.SLASH);
		if (!StringUtils.isBlank(imageName)) {
			imgName.append(imageName);
		}
		return imgName.toString();

	}

	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image by specifying the image type
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticImageUtils(MerchantStoreItem store, String type, String imageName) {
		StringBuilder imgName = new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).append(type).append(Constants.SLASH);
		if (!StringUtils.isBlank(imageName)) {
			imgName.append(imageName);
		}
		return imgName.toString();

	}

	/**
	 * Builds a manufacturer image file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param manufacturer
	 * @param imageName
	 * @return
	 */
	public String buildManufacturerImageUtils(MerchantStoreItem store, ManufacturerItem manufacturer, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).
				append(FileContentType.MANUFACTURER.name()).append(Constants.SLASH)
				.append(manufacturer.getUuid()).append(Constants.SLASH)
				.append(imageName).toString();
	}

	/**
	 * Builds a product image file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param product
	 * @param imageName
	 * @return
	 */
	public String buildProductImageUtils(MerchantStoreItem store, ProductItem product, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.PRODUCTS_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH)
				.append(product.getSku()).append(Constants.SLASH).append(Constants.SMALL_IMAGE).append(Constants.SLASH).append(imageName).toString();
	}

	/**
	 * Builds a default product image file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	public String buildProductImageUtils(MerchantStoreItem store, String sku, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.PRODUCTS_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH)
				.append(sku).append(Constants.SLASH).append(Constants.SMALL_IMAGE).append(Constants.SLASH).append(imageName).toString();
	}

	/**
	 * Builds a large product image file path that can be used by the image servlet
	 *
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	public String buildLargeProductImageUtils(MerchantStoreItem store, String sku, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH)
				.append(sku).append(Constants.SLASH).append(Constants.SMALL_IMAGE).append(Constants.SLASH).append(imageName).toString();
	}


	/**
	 * Builds a merchant store logo path
	 *
	 * @param store
	 * @return
	 */
	public String buildStoreLogoFilePath(MerchantStoreItem store) {
		return new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).append(FileContentType.LOGO).append(Constants.SLASH)
				.append(store.getStoreLogo()).toString();
	}

	/**
	 * Builds product property image url path
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildProductPropertyImageFilePath(MerchantStoreItem store, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).append(FileContentType.PROPERTY).append(Constants.SLASH)
				.append(imageName).toString();
	}

	public String buildProductPropertyImageUtils(MerchantStoreItem store, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append("/").append(FileContentType.PROPERTY).append("/")
				.append(imageName).toString();
	}

	/**
	 * Builds static file url path
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticContentFilePath(MerchantStoreItem store, String fileName) {
		StringBuilder sb = new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH);
		if (!StringUtils.isBlank(fileName)) {
			sb.append(fileName);
		}
		return sb.toString();
	}


}
