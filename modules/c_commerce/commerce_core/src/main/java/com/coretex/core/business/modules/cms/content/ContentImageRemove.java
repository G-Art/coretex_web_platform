package com.coretex.core.business.modules.cms.content;


import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.modules.cms.common.ImageRemove;
import com.coretex.core.model.content.FileContentType;

public interface ContentImageRemove extends ImageRemove {


	void removeImage(final String merchantStoreCode, final FileContentType imageContentType,
					 final String imageName) throws ServiceException;

}
