package com.coretex.core.business.modules.cms.product;


import com.coretex.core.business.modules.cms.common.ImageRemove;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductImageItem;


public interface ProductImageRemove extends ImageRemove {


	void removeProductImage(ProductImageItem productImage);

	void removeProductImages(ProductItem product);


}
