package com.coretex.commerce.core.manager;

import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;

import java.util.List;

public interface ProductImageGet extends ImageGet {

	OutputContentFile getProductImage(final String merchantStoreCode, final String productCode,
									  final String imageName);

	OutputContentFile getProductImage(final String merchantStoreCode, final String productCode,
									  final String imageName, final ProductImageSize size);

	OutputContentFile getProductImage(ProductImageItem productImage) ;

	List<OutputContentFile> getImages(ProductItem product) ;


}
