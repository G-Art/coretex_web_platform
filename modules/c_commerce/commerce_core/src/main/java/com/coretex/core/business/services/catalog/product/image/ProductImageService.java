package com.coretex.core.business.services.catalog.product.image;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.ProductItem;
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
	 * @throws ServiceException
	 */
	void addProductImage(ProductItem product, ProductImageItem productImage, ImageContentFile inputImage)
			throws ServiceException;

	/**
	 * Get the image ByteArrayOutputStream and content description from CMS
	 *
	 * @param productImage
	 * @return
	 * @throws ServiceException
	 */
	OutputContentFile getProductImage(ProductImageItem productImage, ProductImageSize size)
			throws ServiceException;

	/**
	 * Returns all Images for a given product
	 *
	 * @param product
	 * @return
	 * @throws ServiceException
	 */
	List<OutputContentFile> getProductImages(ProductItem product)
			throws ServiceException;

	void removeProductImage(ProductImageItem productImage);

	void saveOrUpdate(ProductImageItem productImage) throws ServiceException;

	/**
	 * Returns an image file from required identifier. This method is
	 * used by the image servlet
	 *
	 * @param store
	 * @param product
	 * @param fileName
	 * @param size
	 * @return
	 * @throws ServiceException
	 */
	OutputContentFile getProductImage(String storeCode, String productCode,
									  String fileName, final ProductImageSize size) throws ServiceException;

	void addProductImages(ProductItem product, List<ImageDataHolder<ProductImageItem>> productImages)
			throws ServiceException;

}
