package com.coretex.shop.utils;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.core.model.content.FileContentType;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.manufacturer.Manufacturer;


/**
 * To be used when working with shopizer servlet for managing images
 * <beans:bean id="img" class="com.coretex.shop.utils.LocalImageFilePathUtils">
 * <beans:property name="basePath" value="/static" />
 * </beans:bean>
 *
 * @author c.samson
 */
@Component
public class LocalImageFilePathUtils extends AbstractimageFilePath {

	private String basePath = Constants.STATIC_URI;

	@Override
	public String getBasePath() {
		return basePath;
	}

	@Override
	public void setBasePath(String context) {
		this.basePath = context;
	}

	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticimageUtils(MerchantStoreItem store, String imageName) {
		StringBuilder imgName = new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append("/").append(FileContentType.IMAGE.name()).append("/");
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
	public String buildStaticimageUtils(MerchantStoreItem store, String type, String imageName) {
		StringBuilder imgName = new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append("/").append(type).append("/");
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
	public String buildManufacturerimageUtils(MerchantStoreItem store, Manufacturer manufacturer, String imageName) {
		return new StringBuilder().append(getBasePath()).append("/").append(store.getCode()).append("/").
				append(FileContentType.MANUFACTURER.name()).append("/")
				.append(manufacturer.getUuid()).append("/")
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
	public String buildProductimageUtils(MerchantStoreItem store, ProductItem product, String imageName) {
		return new StringBuilder().append(getBasePath()).append("/products/").append(store.getCode()).append("/")
				.append(product.getSku()).append("/").append("LARGE").append("/").append(imageName).toString();
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
	public String buildProductimageUtils(MerchantStoreItem store, String sku, String imageName) {
		return new StringBuilder().append(getBasePath()).append("/products/").append(store.getCode()).append("/")
				.append(sku).append("/").append("LARGE").append("/").append(imageName).toString();
	}

	/**
	 * Builds a large product image file path that can be used by the image servlet
	 *
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	public String buildLargeProductimageUtils(MerchantStoreItem store, String sku, String imageName) {
		return new StringBuilder().append(getBasePath()).append("/products/").append(store.getCode()).append("/")
				.append(sku).append("/").append("LARGE").append("/").append(imageName).toString();
	}


	/**
	 * Builds a merchant store logo path
	 *
	 * @param store
	 * @return
	 */
	public String buildStoreLogoFilePath(MerchantStoreItem store) {
		return new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append("/").append(FileContentType.LOGO).append("/")
				.append(store.getStoreLogo()).toString();
	}

	/**
	 * Builds product property image url path
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildProductPropertyimageUtils(MerchantStoreItem store, String imageName) {
		return new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append("/").append(FileContentType.PROPERTY).append("/")
				.append(imageName).toString();
	}

	@Override
	public String getContextPath() {
		return super.getProperties().getProperty(CONTEXT_PATH);
	}


}
