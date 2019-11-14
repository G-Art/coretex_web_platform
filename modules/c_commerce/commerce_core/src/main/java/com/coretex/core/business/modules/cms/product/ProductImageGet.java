package com.coretex.core.business.modules.cms.product;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.modules.cms.common.ImageGet;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.core.model.catalog.product.file.ProductImageSize;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.core.model.content.OutputContentFile;

public interface ProductImageGet extends ImageGet {

	/**
	 * Used for accessing the path directly
	 *
	 * @param merchantStoreCode
	 * @param product
	 * @param imageName
	 * @return
	 * @throws ServiceException
	 */
	OutputContentFile getProductImage(final String merchantStoreCode, final String productCode,
									  final String imageName) throws ServiceException;

	OutputContentFile getProductImage(final String merchantStoreCode, final String productCode,
									  final String imageName, final ProductImageSize size) throws ServiceException;

	OutputContentFile getProductImage(ProductImageItem productImage) throws ServiceException;

	List<OutputContentFile> getImages(ProductItem product) throws ServiceException;


}
