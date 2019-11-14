package com.coretex.shop.store.controller.language.facade;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.store.api.exception.ResourceNotFoundException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class LanguageFacadeImpl implements LanguageFacade {

	@Resource
	private LanguageService languageService;

	@Override
	public List<LanguageItem> getLanguages() {
		try {
			List<LanguageItem> languages = languageService.getLanguages();
			if (languages.isEmpty()) {
				throw new ResourceNotFoundException("No languages found");
			}
			return languages;
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}

	}
}
