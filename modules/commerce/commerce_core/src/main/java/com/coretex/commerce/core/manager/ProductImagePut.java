package com.coretex.commerce.core.manager;

import com.coretex.commerce.core.dto.ImageContentFile;
import com.coretex.items.cx_core.ProductImageItem;

public interface ProductImagePut {

	void addProductImage(ProductImageItem productImage, ImageContentFile contentImage);

}
