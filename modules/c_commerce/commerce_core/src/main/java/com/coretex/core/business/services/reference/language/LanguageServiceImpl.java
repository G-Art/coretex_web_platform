package com.coretex.core.business.services.reference.language;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import com.coretex.core.business.repositories.reference.language.LanguageDao;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.business.utils.CacheUtils;

/**
 * https://samerabdelkafi.wordpress.com/2014/05/29/spring-data-jpa/
 *
 * @author c.samson
 */

@Service("languageService")
public class LanguageServiceImpl extends SalesManagerEntityServiceImpl<LanguageItem>
		implements LanguageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LanguageServiceImpl.class);

	@Resource
	private CacheUtils cache;

	private LanguageDao languageDao;

	public LanguageServiceImpl(LanguageDao languageDao) {
		super(languageDao);
		this.languageDao = languageDao;
	}


	@Override
	public LanguageItem getByCode(String code) throws ServiceException {
		return languageDao.findByCode(code);
	}

	@Override
	public Locale toLocale(LanguageItem language, MerchantStoreItem store) {
		return new Locale(language.getCode());
	}

	@Override
	public LanguageItem toLanguage(Locale locale) {

		try {
			LanguageItem lang = getLanguagesMap().get(locale.getLanguage());
			return lang;
		} catch (Exception e) {
			LOGGER.error("Cannot convert locale " + locale.getLanguage() + " to language");
		}

		var languageItem = new LanguageItem();
		languageItem.setCode(Constants.DEFAULT_LANGUAGE);

		return languageItem;

	}

	@Override
	public Map<String, LanguageItem> getLanguagesMap() throws ServiceException {

		List<LanguageItem> langs = this.getLanguages();
		Map<String, LanguageItem> returnMap = new LinkedHashMap<String, LanguageItem>();

		for (LanguageItem lang : langs) {
			returnMap.put(lang.getCode(), lang);
		}
		return returnMap;

	}


	@Override
	@SuppressWarnings("unchecked")
	public List<LanguageItem> getLanguages() throws ServiceException {


		List<LanguageItem> langs = null;
		try {

			langs = (List<LanguageItem>) cache.getFromCache("LANGUAGES");
			if (langs == null) {
				langs = this.list();
				cache.putInCache(langs, "LANGUAGES");
			}

		} catch (Exception e) {
			LOGGER.error("getCountries()", e);
			throw new ServiceException(e);
		}

		return langs;

	}

	@Override
	public LanguageItem defaultLanguage() {
		return toLanguage(Locale.ENGLISH);
	}

}
