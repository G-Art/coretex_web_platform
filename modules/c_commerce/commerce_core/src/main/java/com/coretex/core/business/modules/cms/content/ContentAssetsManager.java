package com.coretex.core.business.modules.cms.content;

import java.io.Serializable;

import com.coretex.core.business.modules.cms.common.AssetsManager;

public interface ContentAssetsManager
		extends AssetsManager, FileGet, FilePut, FileRemove, Serializable {

}
