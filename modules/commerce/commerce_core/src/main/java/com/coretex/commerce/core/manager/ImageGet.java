package com.coretex.commerce.core.manager;

import com.coretex.commerce.core.dto.FileContentType;

import java.util.List;

public interface ImageGet {

	List<OutputContentFile> getImages(final String merchantStoreCode,
									  FileContentType imageContentType) ;

}