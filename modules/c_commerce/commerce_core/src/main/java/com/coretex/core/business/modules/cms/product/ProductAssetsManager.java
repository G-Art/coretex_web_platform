package com.coretex.core.business.modules.cms.product;

import java.io.Serializable;

import com.coretex.core.business.modules.cms.common.AssetsManager;

public interface ProductAssetsManager
		extends AssetsManager, ProductImageGet, ProductImagePut, ProductImageRemove, Serializable {

}
