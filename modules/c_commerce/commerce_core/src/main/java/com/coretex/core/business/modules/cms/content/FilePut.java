
package com.coretex.core.business.modules.cms.content;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.model.content.InputContentFile;



public interface FilePut {
	void addFile(final String merchantStoreCode, InputContentFile inputStaticContentData)
			throws ServiceException;

	void addFiles(final String merchantStoreCode,
				  List<InputContentFile> inputStaticContentDataList) throws ServiceException;
}
