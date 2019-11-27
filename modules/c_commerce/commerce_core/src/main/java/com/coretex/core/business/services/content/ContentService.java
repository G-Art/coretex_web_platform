package com.coretex.core.business.services.content;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.InputContentFile;
import com.coretex.core.model.content.OutputContentFile;

import java.util.List;


/**
 * Interface defining methods responsible for CMSContentService.
 * ContentServive will be be entry point for CMS and take care of following functionalities.
 * <li>Adding,removing ContentItem images for given merchant store</li>
 * <li>Get,Save,Update ContentItem data for given merchant store</li>
 *
 * @author Umesh Awasthhi
 */
public interface ContentService
{


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


	void addContentFiles(String merchantStoreCode, List<InputContentFile> contentFilesList) throws ServiceException;

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

	OutputContentFile getContentFile(String merchantStoreCode, FileContentType fileContentType, String fileName)
			throws ServiceException;

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
	 * @param cmsContentImage
	 * @throws ServiceException
	 */
	void addOptionImage(String merchantStoreCode, InputContentFile cmsContentImage)
			throws ServiceException;

}
