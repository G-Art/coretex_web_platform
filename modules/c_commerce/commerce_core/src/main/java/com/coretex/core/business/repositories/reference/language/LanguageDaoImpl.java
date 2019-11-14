package com.coretex.core.business.repositories.reference.language;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LanguageDaoImpl extends DefaultGenericDao<LanguageItem> implements LanguageDao {
	public LanguageDaoImpl() {
		super(LanguageItem.ITEM_TYPE);
	}

	@Override
	public LanguageItem findByCode(String code){
		var languageItems = find(Map.of(LanguageItem.CODE, code));
		if(CollectionUtils.isEmpty(languageItems)){
			return null;
		}
		return languageItems.iterator().next();
	}
}
