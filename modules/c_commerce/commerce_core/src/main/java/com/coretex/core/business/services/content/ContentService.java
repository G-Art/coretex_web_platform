package com.coretex.core.business.services.content;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.InputContentFile;
import com.coretex.core.model.content.OutputContentFile;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;


/**
 * Interface defining methods responsible for CMSContentService.
 * ContentServive will be be entry point for CMS and take care of following functionalities.
 * <li>Adding,removing ContentItem images for given merchant store</li>
 * <li>Get,Save,Update ContentItem data for given merchant store</li>
 *
 * @author Umesh Awasthhi
 */
public interface ContentService
		extends SalesManagerEntityService<ContentItem> {

	List<ContentItem> listByType(ContentTypeEnum contentType, MerchantStoreItem store, LanguageItem language)
			throws ServiceException;

	List<ContentItem> listByType(List<ContentTypeEnum> contentType, MerchantStoreItem store, LanguageItem language)
			throws ServiceException;

	ContentItem getByCode(String code, MerchantStoreItem store)
			throws ServiceException;

	void saveOrUpdate(ContentItem content)
			throws ServiceException;

	ContentItem getByCode(String code, MerchantStoreItem store, LanguageItem language)
			throws ServiceException;

	/**
	 * Method responsible for storing content file for given Store.Files for given merchant store will be stored in
	 * Infinispan.
	 *
	 * @param merchantStoreCode merchant store whose content images are being saved.
	 * @param contentFile       content image being stored
	 * @throws ServiceException
	 */
	void addContentFile(String merchantStoreCode, InputContentFile contentFile)
			throws ServiceException;


	/**
	 * Method responsible for storing list of content image for given Store.Images for given merchant store will be stored in
	 * Infinispan.
	 *
	 * @param merchantStoreCode merchant store whose content images are being saved.
	 * @param contentImagesList list of content images being stored.
	 * @throws ServiceException
	 */
	void addContentFiles(String merchantStoreCode, List<InputContentFile> contentFilesList) throws ServiceException;


	/**
	 * Method to remove given content image.Images are stored in underlying system based on there name.
	 * Name will be used to search given image for removal
	 *
	 * @param imageContentType
	 * @param imageName
	 * @param merchantStoreCode merchant store code
	 * @throws ServiceException
	 */
	void removeFile(String merchantStoreCode, FileContentType fileContentType, String fileName) throws ServiceException;

	/**
	 * Removes static file
	 * FileType is no more important
	 *
	 * @param storeCode
	 * @param filename
	 */
	void removeFile(String storeCode, String filename) throws ServiceException;

	/**
	 * Method to remove all images for a given merchant.It will take merchant store as an input and will
	 * remove all images associated with given merchant store.
	 *
	 * @param merchantStoreCode
	 * @throws ServiceException
	 */
	void removeFiles(String merchantStoreCode) throws ServiceException;

	/**
	 * Method responsible for fetching particular content image for a given merchant store. Requested image will be
	 * search in Infinispan tree cache and OutputContentImage will be sent, in case no image is found null will
	 * returned.
	 *
	 * @param merchantStoreCode
	 * @param imageName
	 * @return {@link OutputContentImage}
	 * @throws ServiceException
	 */
	OutputContentFile getContentFile(String merchantStoreCode, FileContentType fileContentType, String fileName)
			throws ServiceException;


	/**
	 * Method to get list of all images associated with a given merchant store.In case of no image method will return an empty list.
	 *
	 * @param merchantStoreCode
	 * @param imageContentType
	 * @return list of {@link OutputContentImage}
	 * @throws ServiceException
	 */
	List<OutputContentFile> getContentFiles(String merchantStoreCode, FileContentType fileContentType)
			throws ServiceException;


	List<String> getContentFilesNames(String merchantStoreCode,
									  FileContentType fileContentType) throws ServiceException;

	/**
	 * Add the store logo
	 *
	 * @param merchantStoreCode
	 * @param cmsContentImage
	 * @throws ServiceException
	 */
	void addLogo(String merchantStoreCode, InputContentFile cmsContentImage)
			throws ServiceException;

	/**
	 * Adds a property (option) image
	 *
	 * @param merchantStoreId
	 * @param cmsContentImage
	 * @throws ServiceException
	 */
	void addOptionImage(String merchantStoreCode, InputContentFile cmsContentImage)
			throws ServiceException;


	List<ContentItem> listByType(List<ContentTypeEnum> contentType, MerchantStoreItem store)
			throws ServiceException;

	List<ContentItem> listNameByType(List<ContentTypeEnum> contentType,
									 MerchantStoreItem store, LanguageItem language) throws ServiceException;

	ContentItem getByLanguage(UUID id, LanguageItem language) throws ServiceException;

	ContentItem getBySeUrl(MerchantStoreItem store, String seUrl);

	/**
	 * Finds content for a specific Merchant for a specific ContentTypeEnum where content
	 * code is like a given prefix in a specific language
	 *
	 * @param type
	 * @param codeLike
	 * @param store
	 * @param lamguage
	 * @return
	 */
	List<ContentItem> getByCodeLike(ContentTypeEnum type, String codeLike, MerchantStoreItem store, LanguageItem language);

}
