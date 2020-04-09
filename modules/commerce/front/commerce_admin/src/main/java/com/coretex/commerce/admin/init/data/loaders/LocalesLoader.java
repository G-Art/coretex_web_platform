package com.coretex.commerce.admin.init.data.loaders;

import com.coretex.commerce.core.init.SchemaConstant;
import com.coretex.commerce.core.init.loader.DataLoader;
import com.coretex.core.activeorm.dao.LocaleDao;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LocalesLoader implements DataLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalesLoader.class);

	@Resource
	private LocaleDao localeDao;

	@Override
	public int priority() {
		return PRIORITY_MAX;
	}

	@Override
	public boolean load(String name) {
		LOGGER.info(String.format("%s : Populating Languages ", name));
		for (String code : SchemaConstant.LANGUAGE_ISO_CODE) {
			var locale = LocaleUtils.toLocale(code);

			LocaleItem localeItem = new LocaleItem();
			localeItem.setIso(code);
			localeItem.setName(locale.getDisplayName());
			localeItem.setName(locale.getDisplayName(locale), locale);
			localeItem.setActive(true);
			localeDao.save(localeItem);
		}
		return true;
	}
}
