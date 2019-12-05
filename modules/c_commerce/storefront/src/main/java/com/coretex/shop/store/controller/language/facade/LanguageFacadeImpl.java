package com.coretex.shop.store.controller.language.facade;

import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.store.api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LanguageFacadeImpl implements LanguageFacade {

	@Resource
	private LanguageService languageService;

	@Override
	public List<LocaleItem> getLanguages() {
		List<LocaleItem> languages = languageService.getLanguages();
		if (languages.isEmpty()) {
			throw new ResourceNotFoundException("No languages found");
		}
		return languages;

	}
}
