package com.coretex.commerce.core.manager;

import com.coretex.commerce.core.constants.Constants;
import com.coretex.commerce.core.dto.FileContentType;
import com.coretex.commerce.core.dto.ImageContentFile;
import com.coretex.items.cx_core.ProductImageItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.StoreItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CmsImageFileManagerImpl implements ProductAssetsManager {


	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CmsImageFileManagerImpl.class);

	private static CmsImageFileManagerImpl fileManager = null;

	private final static String ROOT_NAME = "";

	private final static String SMALL = "SMALL";
	private final static String LARGE = "LARGE";

	private static final String ROOT_CONTAINER = "products";

	private String rootName = ROOT_NAME;

	private LocalCacheManagerImpl cacheManager;

	@PostConstruct
	void init() {

		this.rootName = ((CMSManager) cacheManager).getRootName();
		LOGGER.info("init " + getClass().getName() + " setting root" + this.rootName);

	}

	public static CmsImageFileManagerImpl getInstance() {

		if (fileManager == null) {
			fileManager = new CmsImageFileManagerImpl();
		}

		return fileManager;

	}

	private CmsImageFileManagerImpl() {

	}

	/**
	 * root/products/<merchant id>/<product id>/1.jpg
	 */

	@Override
	public void addProductImage(ProductImageItem productImage, ImageContentFile contentImage)
	{


		try {

			// base path
			String rootPath = this.buildRootPath();
			Path confDir = Paths.get(rootPath);
			this.createDirectoryIfNorExist(confDir);

			// node path
			StringBuilder nodePath = new StringBuilder();
			nodePath.append(rootPath).append(productImage.getProduct().getStore().getCode());
			Path merchantPath = Paths.get(nodePath.toString());
			this.createDirectoryIfNorExist(merchantPath);

			// product path
			nodePath.append(Constants.SLASH).append(productImage.getProduct().getCode())
					.append(Constants.SLASH);
			Path dirPath = Paths.get(nodePath.toString());
			this.createDirectoryIfNorExist(dirPath);

			// small large
			if (contentImage.getFileContentType().name().equals(FileContentType.PRODUCT.name())) {
				nodePath.append(SMALL);
			} else if (contentImage.getFileContentType().name()
					.equals(FileContentType.PRODUCTLG.name())) {
				nodePath.append(LARGE);
			}
			Path sizePath = Paths.get(nodePath.toString());
			this.createDirectoryIfNorExist(sizePath);


			// file creation
			nodePath.append(Constants.SLASH).append(contentImage.getFileName());


			Path path = Paths.get(nodePath.toString());
			InputStream isFile = contentImage.getFile();

			Files.copy(isFile, path, StandardCopyOption.REPLACE_EXISTING);


		} catch (Exception e) {

			throw new RuntimeException(e);

		}

	}

	@Override
	public OutputContentFile getProductImage(ProductImageItem productImage)  {

		// the web server takes care of the images
		return getProductImage(productImage.getProduct().getStore().getCode(),
				productImage.getProduct().getCode(),
				productImage.getProductImage());

	}


	public List<OutputContentFile> getImages(StoreItem store, FileContentType imageContentType)
	{

		// the web server takes care of the images

		return null;

	}

	@Override
	public List<OutputContentFile> getImages(ProductItem product)  {

		// the web server takes care of the images

		return null;
	}


	@Override
	public void removeImages(final String merchantStoreCode)  {

		try {


			StringBuilder merchantPath = new StringBuilder();
			merchantPath.append(buildRootPath()).append(Constants.SLASH).append(merchantStoreCode);

			Path path = Paths.get(merchantPath.toString());

			Files.deleteIfExists(path);


		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}


	@Override
	public void removeProductImage(ProductImageItem productImage) {


		try {


			StringBuilder nodePath = new StringBuilder();
			nodePath.append(buildRootPath()).append(Constants.SLASH)
					.append(productImage.getProduct().getStore().getCode()).append(Constants.SLASH)
					.append(productImage.getProduct().getCode());

			// delete small
			StringBuilder smallPath = new StringBuilder(nodePath);
			smallPath.append(Constants.SLASH).append(SMALL).append(Constants.SLASH)
					.append(productImage.getProductImage());


			Path path = Paths.get(smallPath.toString());

			Files.deleteIfExists(path);

			// delete large
			StringBuilder largePath = new StringBuilder(nodePath);
			largePath.append(Constants.SLASH).append(LARGE).append(Constants.SLASH)
					.append(productImage.getProductImage());


			path = Paths.get(largePath.toString());

			Files.deleteIfExists(path);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}

	@Override
	public void removeProductImages(ProductItem product) {

		try {
			StringBuilder nodePath = new StringBuilder();
			nodePath.append(buildRootPath()).append(Constants.SLASH)
					.append(product.getStore().getCode()).append(Constants.SLASH)
					.append(product.getCode());


			Path path = Paths.get(nodePath.toString());

			Files.deleteIfExists(path);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


	@Override
	public List<OutputContentFile> getImages(final String merchantStoreCode,
											 FileContentType imageContentType)  {

		// the web server taks care of the images

		return null;
	}

	@Override
	public OutputContentFile getProductImage(String merchantStoreCode, String productCode,
											 String imageName)  {
		return getProductImage(merchantStoreCode, productCode, imageName,
				ProductImageSize.SMALL.name());
	}

	@Override
	public OutputContentFile getProductImage(String merchantStoreCode, String productCode,
											 String imageName, ProductImageSize size)  {
		return getProductImage(merchantStoreCode, productCode, imageName, size.name());
	}

	private OutputContentFile getProductImage(String merchantStoreCode, String productCode,
											  String imageName, String size)  {

		StringBuilder nodePath = new StringBuilder();
		nodePath.append(this.buildRootPath()).append(merchantStoreCode)
				.append(Constants.SLASH).append(productCode)
				.append(Constants.SLASH).append(size).append(Constants.SLASH).append(imageName);

		var path = Paths.get(nodePath.toString());
		var contentImage = new OutputContentFile();
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		if(Files.exists(path)){
			try(var input = Files.newInputStream(path)) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				IOUtils.copy(input, output);
				String contentType = fileNameMap.getContentTypeFor(imageName);

				contentImage.setFile(output);
				contentImage.setMimeType(contentType);
				contentImage.setFileName(imageName);
				return contentImage;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		return null;

	}


	private String buildRootPath() {
		return new StringBuilder().append(getRootName()).append(Constants.SLASH).append(ROOT_CONTAINER)
				.append(Constants.SLASH).toString();

	}


	private void createDirectoryIfNorExist(Path path) throws IOException {

		if (Files.notExists(path)) {
			Files.createDirectories(path);
		}
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public String getRootName() {
		return rootName;
	}

	public LocalCacheManagerImpl getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(LocalCacheManagerImpl cacheManager) {
		this.cacheManager = cacheManager;
	}


}
