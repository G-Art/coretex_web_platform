package com.coretex.core.business.modules.cms.product;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.core.model.content.ImageContentFile;


public interface ProductImagePut {


	void addProductImage(ProductImageItem productImage, ImageContentFile contentImage)
			throws ServiceException;


}
