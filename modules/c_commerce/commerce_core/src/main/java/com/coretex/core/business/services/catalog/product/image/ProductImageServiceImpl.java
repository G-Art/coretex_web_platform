package com.coretex.core.business.services.catalog.product.image;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.modules.cms.product.ProductFileManager;
import com.coretex.core.business.repositories.catalog.product.image.ProductImageDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.model.catalog.product.file.ProductImageSize;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.ImageContentFile;
import com.coretex.core.model.content.OutputContentFile;

@Service("productImage")
public class ProductImageServiceImpl extends SalesManagerEntityServiceImpl<ProductImageItem>
		implements ProductImageService {

	private ProductImageDao productImageDao;

	public ProductImageServiceImpl(ProductImageDao productImageDao) {
		super(productImageDao);
		this.productImageDao = productImageDao;
	}

	@Resource
	private ProductFileManager productFileManager;


	public ProductImageItem getByUUID(UUID id) {
		return productImageDao.findOne(id);
	}


	@Override
	public void addProductImages(ProductItem product, List<ImageDataHolder<ProductImageItem>> productImages) throws ServiceException {

		try {
			for (ImageDataHolder<ProductImageItem> productImage : productImages) {

				Assert.notNull(productImage.getImageData());

				InputStream inputStream = productImage.getImageData();
				ImageContentFile cmsContentImage = new ImageContentFile();
				cmsContentImage.setFileName(productImage.getDescriptor().getProductImage());
				cmsContentImage.setFile(inputStream);
				cmsContentImage.setFileContentType(FileContentType.PRODUCT);

				addProductImage(product, productImage.getDescriptor(), cmsContentImage);
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}


	@Override
	public void addProductImage(ProductItem product, ProductImageItem productImage, ImageContentFile inputImage) throws ServiceException {


		productImage.setProduct(product);

		try {

			Assert.notNull(inputImage.getFile(), "ImageContentFile.file cannot be null");


			productFileManager.addProductImage(productImage, inputImage);

			//insert ProductImageItem
			this.saveOrUpdate(productImage);


		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			try {

				if (inputImage.getFile() != null) {
					inputImage.getFile().close();
				}

			} catch (Exception ignore) {

			}
		}


	}

	@Override
	public void saveOrUpdate(ProductImageItem productImage) throws ServiceException {


		super.save(productImage);

	}


	//TODO get default product image


	@Override
	public OutputContentFile getProductImage(ProductImageItem productImage, ProductImageSize size) throws ServiceException {


		ProductImageItem pi = new ProductImageItem();
		String imageName = productImage.getProductImage();
		if (size == ProductImageSize.LARGE) {
			imageName = "L-" + imageName;
		}

		if (size == ProductImageSize.SMALL) {
			imageName = "S-" + imageName;
		}

		pi.setProductImage(imageName);
		pi.setProduct(productImage.getProduct());

		OutputContentFile outputImage = productFileManager.getProductImage(pi);

		return outputImage;

	}

	@Override
	public OutputContentFile getProductImage(final String storeCode, final String productCode, final String fileName, final ProductImageSize size) throws ServiceException {
		OutputContentFile outputImage = productFileManager.getProductImage(storeCode, productCode, fileName, size);
		return outputImage;

	}

	@Override
	public List<OutputContentFile> getProductImages(ProductItem product) throws ServiceException {
		return productFileManager.getImages(product);
	}

	@Override
	public void removeProductImage(ProductImageItem productImage) {

		if (!StringUtils.isBlank(productImage.getProductImage())) {
			productFileManager.removeProductImage(productImage);//managed internally
		}

		ProductImageItem p = this.getByUUID(productImage.getUuid());


		this.delete(p);

	}
}
