package com.coretex.core.business.services.content;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.modules.cms.content.StaticContentFileManager;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.InputContentFile;
import com.coretex.core.model.content.OutputContentFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.net.URLConnection;
import java.util.List;


@Service("contentService")
public class ContentServiceImpl
		implements ContentService {

	private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);

	@Resource
	private StaticContentFileManager contentFileManager;


	/**
	 * Method responsible for adding content file for given merchant store in underlying Infinispan
	 * tree cache. It will take {@link InputContentFile} and will store file for given merchant store
	 * according to its type. it can save an image or any type of file (pdf, css, js ...)
	 *
	 * @param merchantStoreCode Merchant store
	 * @param contentFile       {@link InputContentFile} being stored
	 * @throws ServiceException service exception
	 */
	@Override
	public void addContentFile(String merchantStoreCode, InputContentFile contentFile)
			throws ServiceException {
		Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
		Assert.notNull(contentFile, "InputContentFile image can not be null");
		Assert.notNull(contentFile.getFileName(), "InputContentFile.fileName can not be null");
		Assert.notNull(contentFile.getFileContentType(),
				"InputContentFile.fileContentType can not be null");

		String mimeType = URLConnection.guessContentTypeFromName(contentFile.getFileName());
		contentFile.setMimeType(mimeType);

		if (contentFile.getFileContentType().name().equals(FileContentType.IMAGE.name())
				|| contentFile.getFileContentType().name().equals(FileContentType.STATIC_FILE.name())) {
			addFile(merchantStoreCode, contentFile);
		} else {
			addImage(merchantStoreCode, contentFile);
		}


	}

	@Override
	public void addLogo(String merchantStoreCode, InputContentFile cmsContentImage)
			throws ServiceException {


		Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
		Assert.notNull(cmsContentImage, "CMSContent image can not be null");


		cmsContentImage.setFileContentType(FileContentType.LOGO);
		addImage(merchantStoreCode, cmsContentImage);


	}

	@Override
	public void addOptionImage(String merchantStoreCode, InputContentFile cmsContentImage)
			throws ServiceException {


		Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
		Assert.notNull(cmsContentImage, "CMSContent image can not be null");
		cmsContentImage.setFileContentType(FileContentType.PROPERTY);
		addImage(merchantStoreCode, cmsContentImage);


	}


	private void addImage(String merchantStoreCode, InputContentFile contentImage)
			throws ServiceException {

		try {
			LOG.info("Adding content image for merchant id {}", merchantStoreCode);
			contentFileManager.addFile(merchantStoreCode, contentImage);

		} catch (Exception e) {
			LOG.error("Error while trying to convert input stream to buffered image", e);
			throw new ServiceException(e);

		} finally {

			try {
				if (contentImage.getFile() != null) {
					contentImage.getFile().close();
				}
			} catch (Exception ignore) {
			}

		}

	}

	private void addFile(final String merchantStoreCode, InputContentFile contentImage)
			throws ServiceException {

		try {
			LOG.info("Adding content file for merchant id {}", merchantStoreCode);
			// staticContentFileManager.addFile(merchantStoreCode, contentImage);
			contentFileManager.addFile(merchantStoreCode, contentImage);

		} catch (Exception e) {
			LOG.error("Error while trying to convert input stream to buffered image", e);
			throw new ServiceException(e);

		} finally {

			try {
				if (contentImage.getFile() != null) {
					contentImage.getFile().close();
				}
			} catch (Exception ignore) {
			}
		}

	}

	@Override
	public void addContentFiles(String merchantStoreCode, List<InputContentFile> contentFilesList)
			throws ServiceException {

		Assert.notNull(merchantStoreCode, "Merchant store ID can not be null");
		Assert.notEmpty(contentFilesList, "File list can not be empty");
		LOG.info("Adding total {} images for given merchant", contentFilesList.size());

		LOG.info("Adding content images for merchant....");
		contentFileManager.addFiles(merchantStoreCode, contentFilesList);
		// staticContentFileManager.addFiles(merchantStoreCode, contentFilesList);

		try {
			for (InputContentFile file : contentFilesList) {
				if (file.getFile() != null) {
					file.getFile().close();
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void removeFile(String merchantStoreCode, FileContentType fileContentType, String fileName)
			throws ServiceException {
		Assert.notNull(merchantStoreCode, "Merchant Store Id can not be null");
		Assert.notNull(fileContentType, "ContentItem file type can not be null");
		Assert.notNull(fileName, "ContentItem Image type can not be null");


		contentFileManager.removeFile(merchantStoreCode, fileContentType, fileName);


	}

	@Override
	public void removeFile(String storeCode, String fileName) throws ServiceException {

		String fileType = "IMAGE";
		String mimetype = URLConnection.guessContentTypeFromName(fileName);
		String type = mimetype.split("/")[0];
		if (!type.equals("image"))
			fileType = "STATIC_FILE";

		contentFileManager.removeFile(storeCode, FileContentType.valueOf(fileType), fileName);

	}

	/**
	 * Method to remove all images for a given merchant.It will take merchant store as an input and
	 * will remove all images associated with given merchant store.
	 *
	 * @param merchantStoreCode
	 * @throws ServiceException
	 */
	@Override
	public void removeFiles(String merchantStoreCode) throws ServiceException {
		Assert.notNull(merchantStoreCode, "Merchant Store Id can not be null");


		contentFileManager.removeFiles(merchantStoreCode);

		// staticContentFileManager.removeFiles(merchantStoreCode);

	}

	@Override
	public OutputContentFile getContentFile(String merchantStoreCode, FileContentType fileContentType,
											String fileName) throws ServiceException {
		Assert.notNull(merchantStoreCode, "Merchant store ID can not be null");
		Assert.notNull(fileName, "File name can not be null");

		if (fileContentType.name().equals(FileContentType.IMAGE.name())
				|| fileContentType.name().equals(FileContentType.STATIC_FILE.name())) {
			return contentFileManager.getFile(merchantStoreCode, fileContentType, fileName);

		} else {
			return contentFileManager.getFile(merchantStoreCode, fileContentType, fileName);
		}


	}

	@Override
	public List<OutputContentFile> getContentFiles(String merchantStoreCode,
												   FileContentType fileContentType) throws ServiceException {
		Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
		// return staticContentFileManager.getFiles(merchantStoreCode, fileContentType);
		return contentFileManager.getFiles(merchantStoreCode, fileContentType);
	}

	@Override
	public List<String> getContentFilesNames(String merchantStoreCode,
											 FileContentType fileContentType) throws ServiceException {
		Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");

		return contentFileManager.getFileNames(merchantStoreCode, fileContentType);

		/*
		 * if(fileContentType.name().equals(FileContentType.IMAGE.name()) ||
		 * fileContentType.name().equals(FileContentType.STATIC_FILE.name())) { return
		 * contentFileManager.getFileNames(merchantStoreCode, fileContentType); } else { return
		 * contentFileManager.getFileNames(merchantStoreCode, fileContentType); }
		 */
	}

}
