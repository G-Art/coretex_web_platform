package com.coretex.core.business.services.reference.language;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.core.LocaleItem;

public interface LanguageService extends SalesManagerEntityService<LocaleItem> {

	LocaleItem getByCode(String code);

	Map<String, LocaleItem> getLanguagesMap() throws ServiceException;

	List<LocaleItem> getLanguages() throws ServiceException;

	Locale toLocale(LocaleItem language, MerchantStoreItem store);

	LocaleItem toLanguage(Locale locale);

	LocaleItem defaultLanguage();
}
