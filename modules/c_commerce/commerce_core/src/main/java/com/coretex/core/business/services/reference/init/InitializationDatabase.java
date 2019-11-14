package com.coretex.core.business.services.reference.init;

import com.coretex.core.business.exception.ServiceException;

public interface InitializationDatabase {

	boolean isEmpty();

	void populate(String name) throws ServiceException;

}
