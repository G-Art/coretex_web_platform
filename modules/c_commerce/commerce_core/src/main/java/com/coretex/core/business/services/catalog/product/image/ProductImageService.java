package com.coretex.core.business.services.catalog.product.image;

import java.util.List;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.core.model.catalog.product.file.ProductImageSize;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.core.model.content.ImageContentFile;
import com.coretex.core.model.content.OutputContentFile;


public interface ProductImageService extends SalesManagerEntityService<ProductImageItem> {


	/**
	 * Add a ProductImageItem to the persistence and an entry to the CMS
	 *
	 * @param product
	 * @param productImage
	 * @param file
	 * @
	 */
	void addProductImage(ProductItem product, ProductImageItem productImage, ImageContentFile inputImage)
			;

	/**
	 * Get the image ByteArrayOutputStream and content description from CMS
	 *
	 * @param productImage
	 * @return
	 * @
	 */
	OutputContentFile getProductImage(ProductImageItem productImage, ProductImageSize size)
			;

	/**
	 * Returns all Images for a given product
	 *
	 * @param product
	 * @return
	 * @
	 */
	List<OutputContentFile> getProductImages(ProductItem product)
			;

	void removeProductImage(ProductImageItem productImage);

	void saveOrUpdate(ProductImageItem productImage) ;

	/**
	 * Returns an image file from required identifier. This method is
	 * used by the image servlet
	 *
	 * @param store
	 * @param product
	 * @param fileName
	 * @param size
	 * @return
	 * @
	 */
	OutputContentFile getProductImage(String storeCode, String productCode,
									  String fileName, final ProductImageSize size);

	void addProductImages(ProductItem product, List<ImageDataHolder<ProductImageItem>> productImages)
			;

}
