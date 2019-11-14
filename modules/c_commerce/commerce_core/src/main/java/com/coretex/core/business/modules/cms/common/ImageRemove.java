package com.coretex.core.business.modules.cms.common;

import com.coretex.core.business.exception.ServiceException;


public interface ImageRemove {


	void removeImages(final String merchantStoreCode) throws ServiceException;

}
