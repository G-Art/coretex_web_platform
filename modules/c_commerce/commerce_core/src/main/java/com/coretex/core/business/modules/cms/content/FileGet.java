package com.coretex.core.business.modules.cms.content;

import java.util.List;


import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.OutputContentFile;


/**
 * Methods to retrieve the static content from the CMS
 *
 * @author Carl Samson
 */
public interface FileGet {

	OutputContentFile getFile(final String merchantStoreCode, FileContentType fileContentType,
							  String contentName) ;

	List<String> getFileNames(final String merchantStoreCode, FileContentType fileContentType)
			;

	List<OutputContentFile> getFiles(final String merchantStoreCode,
									 FileContentType fileContentType) ;
}
