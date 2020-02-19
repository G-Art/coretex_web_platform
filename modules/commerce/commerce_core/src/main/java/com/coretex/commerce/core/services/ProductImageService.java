package com.coretex.commerce.core.services;

import com.coretex.commerce.core.dto.ImageContentFile;
import com.coretex.commerce.core.dto.ImageDataHolder;
import com.coretex.commerce.core.manager.OutputContentFile;
import com.coretex.commerce.core.manager.ProductImageSize;
import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;

import java.util.List;

public interface ProductImageService extends GenericItemService<ProductImageItem> {

	void addProductImage(ProductItem product, ProductImageItem productImage, ImageContentFile inputImage);

	OutputContentFile getProductImage(ProductImageItem productImage, ProductImageSize size);

	List<OutputContentFile> getProductImages(ProductItem product);

	void removeProductImage(ProductImageItem productImage);

	OutputContentFile getProductImage(String storeCode, String productCode,
									  String fileName, final ProductImageSize size);

	void addProductImages(ProductItem product, List<ImageDataHolder<ProductImageItem>> productImages);

}
