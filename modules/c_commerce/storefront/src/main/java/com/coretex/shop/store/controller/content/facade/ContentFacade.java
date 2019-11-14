package com.coretex.shop.store.controller.content.facade;

import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.content.ContentFile;

import java.util.List;

import com.coretex.shop.model.content.ContentFolder;
import com.coretex.shop.model.content.PersistableContentPage;
import com.coretex.shop.model.content.ReadableContentBox;
import com.coretex.shop.model.content.ReadableContentPage;

/**
 * Images and files management
 *
 * @author carlsamson
 */
public interface ContentFacade {


	ContentFolder getContentFolder(String folder, MerchantStoreItem store) throws Exception;

	/**
	 * File pth
	 *
	 * @param store
	 * @param file
	 * @return
	 */
	String absolutePath(MerchantStoreItem store, String file);

	/**
	 * Deletes a file from CMS
	 *
	 * @param store
	 * @param fileName
	 */
	void delete(MerchantStoreItem store, String fileName, String fileType);


	/**
	 * Returns page names and urls configured for a given MerchantStoreItem
	 *
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	List<ReadableContentPage> getContentPage(MerchantStoreItem store, LanguageItem language);


	/**
	 * Returns page name by code
	 *
	 * @param code
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableContentPage getContentPage(String code, MerchantStoreItem store, LanguageItem language);


	/**
	 * Returns a content box for a given code and merchant store
	 *
	 * @param code
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableContentBox getContentBox(String code, MerchantStoreItem store, LanguageItem language);


	/**
	 * Returns content boxes created with code prefix
	 * for example return boxes with code starting with <code>_
	 *
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	List<ReadableContentBox> getContentBoxes(ContentTypeEnum type, String codePrefix, MerchantStoreItem store, LanguageItem language);

	void addContentFile(ContentFile file, String merchantStoreCode);

	/**
	 * Save content page
	 *
	 * @param page
	 * @param merchantStore
	 * @param language
	 */
	void saveContentPage(PersistableContentPage page, MerchantStoreItem merchantStore, LanguageItem language);

}
