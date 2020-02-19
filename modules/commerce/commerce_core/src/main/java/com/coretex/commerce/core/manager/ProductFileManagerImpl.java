package com.coretex.commerce.core.manager;

import com.coretex.commerce.core.dto.FileContentType;
import com.coretex.commerce.core.dto.ImageContentFile;
import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class ProductFileManagerImpl extends ProductFileManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductFileManagerImpl.class);


	private ProductImagePut uploadImage;
	private ProductImageGet getImage;
	private ProductImageRemove removeImage;

//	private CoreConfiguration configuration;

	private final static String PRODUCT_IMAGE_HEIGHT_SIZE = "PRODUCT_IMAGE_HEIGHT_SIZE";
	private final static String PRODUCT_IMAGE_WIDTH_SIZE = "PRODUCT_IMAGE_WIDTH_SIZE";
	private final static String CROP_UPLOADED_IMAGES = "CROP_UPLOADED_IMAGES";


//	public CoreConfiguration getConfiguration() {
//		return configuration;
//	}


//	public void setConfiguration(CoreConfiguration configuration) {
//		this.configuration = configuration;
//	}


	public ProductImageRemove getRemoveImage() {
		return removeImage;
	}


	public void setRemoveImage(ProductImageRemove removeImage) {
		this.removeImage = removeImage;
	}


	public void addProductImage(ProductImageItem productImage, ImageContentFile contentImage) {

		try {

			/** copy to input stream **/
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// Fake code simulating the copy
			// You can generally do better with nio if you need...
			// And please, unlike me, do something about the Exceptions :D
			byte[] buffer = new byte[1024];
			int len;
			while ((len = contentImage.getFile().read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();

			// Open new InputStreams using the recorded bytes
			// Can be repeated as many times as you wish
			InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
			InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

			BufferedImage bufferedImage = ImageIO.read(is2);


			if (bufferedImage == null) {
				LOGGER.error("Cannot read image format for " + productImage.getProductImage());
				throw new Exception("Cannot read image format " + productImage.getProductImage());
			}

			// contentImage.setBufferedImage(bufferedImage);
			contentImage.setFile(is1);

			// upload original -- L
			contentImage.setFileContentType(FileContentType.PRODUCT);
			uploadImage.addProductImage(productImage, contentImage);


		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


	public OutputContentFile getProductImage(ProductImageItem productImage) {
		// will return original
		return getImage.getProductImage(productImage);
	}


	@Override
	public List<OutputContentFile> getImages(final String merchantStoreCode,
											 FileContentType imageContentType) {
		// will return original
		return getImage.getImages(merchantStoreCode, FileContentType.PRODUCT);
	}

	@Override
	public List<OutputContentFile> getImages(ProductItem product) {
		return getImage.getImages(product);
	}


	@Override
	public void removeProductImage(ProductImageItem productImage) {

		this.removeImage.removeProductImage(productImage);

		/*
		 * ProductImageItem large = new ProductImageItem(); large.setProduct(productImage.getProduct());
		 * large.setProductImage("L" + productImage.getProductImage());
		 *
		 * this.removeImage.removeProductImage(large);
		 *
		 * ProductImageItem small = new ProductImageItem(); small.setProduct(productImage.getProduct());
		 * small.setProductImage("S" + productImage.getProductImage());
		 *
		 * this.removeImage.removeProductImage(small);
		 */

	}


	@Override
	public void removeProductImages(ProductItem product) {

		this.removeImage.removeProductImages(product);

	}


	@Override
	public void removeImages(final String merchantStoreCode) {

		this.removeImage.removeImages(merchantStoreCode);

	}


	public ProductImagePut getUploadImage() {
		return uploadImage;
	}


	public void setUploadImage(ProductImagePut uploadImage) {
		this.uploadImage = uploadImage;
	}


	public ProductImageGet getGetImage() {
		return getImage;
	}


	public void setGetImage(ProductImageGet getImage) {
		this.getImage = getImage;
	}


	@Override
	public OutputContentFile getProductImage(String merchantStoreCode, String productCode,
											 String imageName) {
		return getImage.getProductImage(merchantStoreCode, productCode, imageName);
	}


	@Override
	public OutputContentFile getProductImage(String merchantStoreCode, String productCode,
											 String imageName, ProductImageSize size) {
		return getImage.getProductImage(merchantStoreCode, productCode, imageName, size);
	}


}
