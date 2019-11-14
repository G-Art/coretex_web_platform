package com.coretex.core.business.repositories.content;

import java.util.List;

import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;


public interface ContentRepositoryCustom {

	List<ContentItem> listNameByType(List<ContentTypeEnum> contentType,
									 MerchantStoreItem store, LanguageItem language);

	ContentItem getBySeUrl(MerchantStoreItem store, String seUrl);


}
