package com.coretex.core.business.repositories.reference.language;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.items.commerce_core_model.LanguageItem;

public interface LanguageDao extends Dao<LanguageItem> {

	LanguageItem findByCode(String code);


}
