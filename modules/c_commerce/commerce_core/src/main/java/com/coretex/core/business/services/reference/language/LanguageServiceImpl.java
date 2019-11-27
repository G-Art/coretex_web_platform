package com.coretex.core.business.services.reference.language;

import com.coretex.core.activeorm.dao.LocaleDao;
import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.business.utils.CacheUtils;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * https://samerabdelkafi.wordpress.com/2014/05/29/spring-data-jpa/
 *
 * @author c.samson
 */

@Service("languageService")
public class LanguageServiceImpl extends SalesManagerEntityServiceImpl<LocaleItem>
		implements LanguageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LanguageServiceImpl.class);

	@Resource
	private CacheUtils cache;

	private LocaleDao languageDao;

	public LanguageServiceImpl(LocaleDao languageDao) {
		super(languageDao);
		this.languageDao = languageDao;
	}


	@Override
	public LocaleItem getByCode(String code) {
		return languageDao.findSingle(Map.of(LocaleItem.ISO, code), true);
	}

	@Override
	public Locale toLocale(LocaleItem language, MerchantStoreItem store) {
		return new Locale(language.getIso());
	}

	@Override
	public LocaleItem toLanguage(Locale locale) {

		try {
			LocaleItem lang = getLanguagesMap().get(locale.getLanguage());
			return lang;
		} catch (Exception e) {
			LOGGER.error("Cannot convert locale " + locale.getLanguage() + " to language");
		}

		var languageItem = new LocaleItem();
		languageItem.setIso(Constants.DEFAULT_LANGUAGE);

		return languageItem;

	}

	@Override
	public Map<String, LocaleItem> getLanguagesMap() throws ServiceException {

		List<LocaleItem> langs = this.getLanguages();
		Map<String, LocaleItem> returnMap = new LinkedHashMap<String, LocaleItem>();

		for (LocaleItem lang : langs) {
			returnMap.put(lang.getIso(), lang);
		}
		return returnMap;

	}


	@Override
	@SuppressWarnings("unchecked")
	public List<LocaleItem> getLanguages() throws ServiceException {


		List<LocaleItem> langs = null;
		try {

			langs = (List<LocaleItem>) cache.getFromCache("LANGUAGES");
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
	public LocaleItem defaultLanguage() {
		return toLanguage(Locale.ENGLISH);
	}

}
