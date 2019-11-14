package com.coretex.core.business.modules.cms.content;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.modules.cms.common.ImageGet;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.OutputContentFile;

public interface ContentImageGet extends ImageGet {

	OutputContentFile getImage(final String merchantStoreCode, String imageName,
							   FileContentType imageContentType) throws ServiceException;

	List<String> getImageNames(final String merchantStoreCode,
							   FileContentType imageContentType) throws ServiceException;

}
