package com.coretex.commerce.core.manager;

import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;

public interface ProductImageRemove extends ImageRemove {

	void removeProductImage(ProductImageItem productImage);

	void removeProductImages(ProductItem product);


}
