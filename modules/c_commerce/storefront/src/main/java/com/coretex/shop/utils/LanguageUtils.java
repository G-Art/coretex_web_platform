package com.coretex.shop.utils;

import java.util.Locale;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;

@Component
public class LanguageUtils {

	protected final Log logger = LogFactory.getLog(getClass());

	@Resource
	LanguageService languageService;

	public LocaleItem getServiceLanguage(String lang) {
		LocaleItem l = null;
		if (!StringUtils.isBlank(lang)) {
			l = languageService.getByCode(lang);
		}

		if (l == null) {
			l = languageService.defaultLanguage();
		}

		return l;
	}

	/**
	 * Determines request language based on store rules
	 *
	 * @param request
	 * @return
	 */
	public LocaleItem getRequestLanguage(HttpServletRequest request, HttpServletResponse response) {

		Locale locale = null;

		LocaleItem language = (LocaleItem) request.getSession().getAttribute(Constants.LANGUAGE);
		MerchantStoreItem store = (MerchantStoreItem) request.getSession().getAttribute(Constants.MERCHANT_STORE);


		if (language == null) {
			try {

				locale = LocaleContextHolder.getLocale();//should be browser locale


				if (store != null) {
					language = store.getDefaultLanguage();
					if (language != null) {
						locale = languageService.toLocale(language, store);
						if (locale != null) {
							LocaleContextHolder.setLocale(locale);
						}
						request.getSession().setAttribute(Constants.LANGUAGE, language);
					}

					if (language == null) {
						language = languageService.toLanguage(locale);
						request.getSession().setAttribute(Constants.LANGUAGE, language);
					}

				}

			} catch (Exception e) {
				if (language == null) {
					try {
						language = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
					} catch (Exception ignore) {
					}
				}
			}
		} else {


			Locale localeFromContext = LocaleContextHolder.getLocale();//should be browser locale
			if (!language.getIso().equals(localeFromContext.getLanguage())) {
				//get locale context
				language = languageService.toLanguage(localeFromContext);
			}

		}

		if (language != null) {
			locale = languageService.toLocale(language, store);
		} else {
			language = languageService.toLanguage(locale);
		}

		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		if (localeResolver != null) {
			localeResolver.setLocale(request, response, locale);
		}
		response.setLocale(locale);
		request.getSession().setAttribute(Constants.LANGUAGE, language);

		return language;
	}

	/**
	 * Should be used by rest web services
	 *
	 * @param request
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public LocaleItem getRESTLanguage(HttpServletRequest request, MerchantStoreItem store) {

		Validate.notNull(request, "HttpServletRequest must not be null");
		Validate.notNull(store, "MerchantStoreItem must not be null");

		LocaleItem language = null;

		String lang = request.getParameter(Constants.LANG);

		if (StringUtils.isBlank(lang)) {
			if (language == null) {
				language = languageService.defaultLanguage();
			}
		} else {
			language = languageService.getByCode(lang);
			if (language == null) {
				language = languageService.defaultLanguage();
			}
		}

		return language;

	}

}
