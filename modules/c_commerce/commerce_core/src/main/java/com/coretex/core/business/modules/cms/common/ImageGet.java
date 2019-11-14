package com.coretex.core.business.modules.cms.common;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.OutputContentFile;

public interface ImageGet {

	List<OutputContentFile> getImages(final String merchantStoreCode,
									  FileContentType imageContentType) throws ServiceException;

}
