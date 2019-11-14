
package com.coretex.core.business.modules.cms.content;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.model.content.FileContentType;



public interface FileRemove {
	void removeFile(String merchantStoreCode, FileContentType staticContentType,
					String fileName);

	void removeFiles(String merchantStoreCode) throws ServiceException;

}
