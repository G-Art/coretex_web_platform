package com.coretex.core.business.modules.cms.common;

import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.OutputContentFile;

import java.util.List;

public interface ImageGet {

	List<OutputContentFile> getImages(final String merchantStoreCode,
									  FileContentType imageContentType) ;

}
