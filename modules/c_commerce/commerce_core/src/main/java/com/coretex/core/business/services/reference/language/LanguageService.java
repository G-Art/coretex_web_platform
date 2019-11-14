package com.coretex.core.business.services.reference.language;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;

public interface LanguageService extends SalesManagerEntityService<LanguageItem> {

	LanguageItem getByCode(String code) throws ServiceException;

	Map<String, LanguageItem> getLanguagesMap() throws ServiceException;

	List<LanguageItem> getLanguages() throws ServiceException;

	Locale toLocale(LanguageItem language, MerchantStoreItem store);

	LanguageItem toLanguage(Locale locale);

	LanguageItem defaultLanguage();
}
