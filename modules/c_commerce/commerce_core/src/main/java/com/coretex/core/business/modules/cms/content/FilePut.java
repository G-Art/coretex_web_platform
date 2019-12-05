
package com.coretex.core.business.modules.cms.content;

import java.util.List;


import com.coretex.core.model.content.InputContentFile;



public interface FilePut {
	void addFile(final String merchantStoreCode, InputContentFile inputStaticContentData)
			;

	void addFiles(final String merchantStoreCode,
				  List<InputContentFile> inputStaticContentDataList) ;
}
