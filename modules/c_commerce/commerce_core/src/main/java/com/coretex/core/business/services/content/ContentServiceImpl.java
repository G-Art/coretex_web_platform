package com.coretex.core.business.services.content;

import java.net.URLConnection;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.modules.cms.content.StaticContentFileManager;
import com.coretex.core.business.repositories.content.ContentDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.InputContentFile;
import com.coretex.core.model.content.OutputContentFile;


@Service("contentService")
public class ContentServiceImpl extends SalesManagerEntityServiceImpl<ContentItem>
		implements ContentService {

	private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);

	private final ContentDao contentDao;

	@Resource
	private StaticContentFileManager contentFileManager;

	public ContentServiceImpl(ContentDao contentDao) {
		super(contentDao);

		this.contentDao = contentDao;
	}

	@Override
	public List<ContentItem> listByType(ContentTypeEnum contentType, MerchantStoreItem store, LanguageItem language)
			throws ServiceException {

		return contentDao.findByType(contentType, store.getUuid(), language.getUuid());
	}

	@Override
	public ContentItem getByLanguage(UUID id, LanguageItem language) throws ServiceException {
		return contentDao.findByIdAndLanguage(id, language.getUuid());
	}


	@Override
	public List<ContentItem> listByType(List<ContentTypeEnum> contentType, MerchantStoreItem store,
										LanguageItem language) throws ServiceException {

		/*
		 * List<String> contentTypes = new ArrayList<String>(); for (int i = 0; i < contentType.size();
		 * i++) { contentTypes.add(contentType.get(i).name()); }
		 */

		return contentDao.findByTypes(contentType, store.getUuid(), language.getUuid());
	}

	@Override
	public List<ContentItem> listNameByType(List<ContentTypeEnum> contentType, MerchantStoreItem store,
											LanguageItem language) throws ServiceException {


		return contentDao.listNameByType(contentType, store, language);
	}

	@Override
	public List<ContentItem> listByType(List<ContentTypeEnum> contentType, MerchantStoreItem store)
			throws ServiceException {
		/*
		 * List<String> contentTypes = new ArrayList<String>(); for (int i = 0; i < contentType.size();
		 * i++) { contentTypes.add(contentType.get(i).name()); }
		 */

		return contentDao.findByTypes(contentType, store.getUuid());
	}

	@Override
	public ContentItem getByCode(String code, MerchantStoreItem store) throws ServiceException {


		return contentDao.findByCode(code, store.getUuid());

	}

	@Override
	public void saveOrUpdate(final ContentItem content) throws ServiceException {

		super.save(content);
	}

	@Override
	public ContentItem getByCode(String code, MerchantStoreItem store, LanguageItem language)
			throws ServiceException {
		return contentDao.findByCode(code, store.getUuid(), language.getUuid());
	}

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


	/**
	 * Method responsible for adding list of content images for given merchant store in underlying
	 * Infinispan tree cache. It will take list of {@link CMSContentImage} and will store them for
	 * given merchant store.
	 *
	 * @param merchantStoreCode Merchant store
	 * @param contentImagesList list of {@link CMSContentImage} being stored
	 * @throws ServiceException service exception
	 */
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

	/**
	 * Method to remove given content image.Images are stored in underlying system based on there
	 * name. Name will be used to search given image for removal
	 *
	 * @param contentImage
	 * @param merchantStoreCode merchant store
	 * @throws ServiceException
	 */
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


	/**
	 * Implementation for getContentImage method defined in {@link ContentService} interface. Methods
	 * will return ContentItem image with given image name for the Merchant store or will return null if
	 * no image with given name found for requested Merchant Store in Infinispan tree cache.
	 *
	 * @param store     Merchant merchantStoreCode
	 * @param imageName name of requested image
	 * @return {@link OutputContentImage}
	 * @throws ServiceException
	 */
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

	/**
	 * Implementation for getContentImages method defined in {@link ContentService} interface. Methods
	 * will return list of all ContentItem image associated with given Merchant store or will return empty
	 * list if no image is associated with given Merchant Store in Infinispan tree cache.
	 *
	 * @param merchantStoreId Merchant store
	 * @return list of {@link OutputContentImage}
	 * @throws ServiceException
	 */
	@Override
	public List<OutputContentFile> getContentFiles(String merchantStoreCode,
												   FileContentType fileContentType) throws ServiceException {
		Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
		// return staticContentFileManager.getFiles(merchantStoreCode, fileContentType);
		return contentFileManager.getFiles(merchantStoreCode, fileContentType);
	}

	/**
	 * Returns the image names for a given merchant and store
	 *
	 * @param merchantStoreCode
	 * @param imageContentType
	 * @return images name list
	 * @throws ServiceException
	 */
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

	@Override
	public ContentItem getBySeUrl(MerchantStoreItem store, String seUrl) {
		return contentDao.getBySeUrl(store, seUrl);
	}

	@Override
	public List<ContentItem> getByCodeLike(ContentTypeEnum type, String codeLike, MerchantStoreItem store,
										   LanguageItem language) {
		return contentDao.findByCodeLike(type, '%' + codeLike + '%', store.getUuid(),
				language.getUuid());
	}


}
