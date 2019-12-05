package com.coretex.core.business.modules.cms.product;

import java.util.List;


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
	 * @
	 */
	OutputContentFile getProductImage(final String merchantStoreCode, final String productCode,
									  final String imageName);

	OutputContentFile getProductImage(final String merchantStoreCode, final String productCode,
									  final String imageName, final ProductImageSize size);

	OutputContentFile getProductImage(ProductImageItem productImage) ;

	List<OutputContentFile> getImages(ProductItem product) ;


}
