package com.coretex.core.business.services.content;


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
	 * @
	 */
	void addContentFile(String merchantStoreCode, InputContentFile contentFile)
			;


	void addContentFiles(String merchantStoreCode, List<InputContentFile> contentFilesList) ;

	void removeFile(String merchantStoreCode, FileContentType fileContentType, String fileName) ;

	/**
	 * Removes static file
	 * FileType is no more important
	 *
	 * @param storeCode
	 * @param filename
	 */
	void removeFile(String storeCode, String filename) ;

	/**
	 * Method to remove all images for a given merchant.It will take merchant store as an input and will
	 * remove all images associated with given merchant store.
	 *
	 * @param merchantStoreCode
	 * @
	 */
	void removeFiles(String merchantStoreCode) ;

	OutputContentFile getContentFile(String merchantStoreCode, FileContentType fileContentType, String fileName)
			;

	List<OutputContentFile> getContentFiles(String merchantStoreCode, FileContentType fileContentType)
			;


	List<String> getContentFilesNames(String merchantStoreCode,
									  FileContentType fileContentType) ;

	/**
	 * Add the store logo
	 *
	 * @param merchantStoreCode
	 * @param cmsContentImage
	 * @
	 */
	void addLogo(String merchantStoreCode, InputContentFile cmsContentImage)
			;

	/**
	 * Adds a property (option) image
	 *
	 * @param cmsContentImage
	 * @
	 */
	void addOptionImage(String merchantStoreCode, InputContentFile cmsContentImage)
			;

}
