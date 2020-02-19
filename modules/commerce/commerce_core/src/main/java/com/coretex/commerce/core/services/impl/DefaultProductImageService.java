package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.ProductImageDao;
import com.coretex.commerce.core.dto.FileContentType;
import com.coretex.commerce.core.dto.ImageContentFile;
import com.coretex.commerce.core.dto.ImageDataHolder;
import com.coretex.commerce.core.manager.OutputContentFile;
import com.coretex.commerce.core.manager.ProductFileManager;
import com.coretex.commerce.core.manager.ProductImageSize;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.ProductImageService;
import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

@Service
public class DefaultProductImageService extends AbstractGenericItemService<ProductImageItem> implements ProductImageService {

	private ProductImageDao productImageDao;

	public DefaultProductImageService(ProductImageDao productImageDao) {
		super(productImageDao);
		this.productImageDao = productImageDao;
	}

	@Resource
	private ProductFileManager productFileManager;

	@Override
	public void addProductImages(ProductItem product, List<ImageDataHolder<ProductImageItem>> productImages)  {

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
			throw new RuntimeException(e);
		}

	}


	@Override
	public void addProductImage(ProductItem product, ProductImageItem productImage, ImageContentFile inputImage)  {


		productImage.setProduct(product);

		try {

			Assert.notNull(inputImage.getFile(), "ImageContentFile.file cannot be null");


			productFileManager.addProductImage(productImage, inputImage);


		} catch (Exception e) {
			throw new RuntimeException(e);
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
	public OutputContentFile getProductImage(ProductImageItem productImage, ProductImageSize size)  {


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
	public OutputContentFile getProductImage(final String storeCode, final String productCode, final String fileName, final ProductImageSize size){
		OutputContentFile outputImage = productFileManager.getProductImage(storeCode, productCode, fileName, size);
		return outputImage;

	}

	@Override
	public List<OutputContentFile> getProductImages(ProductItem product)  {
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
