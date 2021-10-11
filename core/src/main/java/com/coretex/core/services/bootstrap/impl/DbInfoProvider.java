package com.coretex.core.services.bootstrap.impl;

import com.coretex.core.activeorm.translator.TypeTranslator;
import com.coretex.core.services.bootstrap.dialect.DbDialectService;

public interface DbInfoProvider {


	TypeTranslator<?> getTypeTranslator(Class<?> typeClass);

	DbDialectService getDbDialectService();
}
