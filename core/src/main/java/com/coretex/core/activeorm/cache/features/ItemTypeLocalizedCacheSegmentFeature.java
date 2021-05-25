package com.coretex.core.activeorm.cache.features;

import com.coretex.core.activeorm.cache.CacheContext;
import com.coretex.core.activeorm.cache.impl.FeaturedStatementCacheContext;
import com.coretex.core.services.bootstrap.impl.CortexContext;

public class ItemTypeLocalizedCacheSegmentFeature extends ItemTypeCacheSegmentFeature {

	public ItemTypeLocalizedCacheSegmentFeature(String type, CortexContext cortexContext) {
		super(type, cortexContext);
	}

	public ItemTypeLocalizedCacheSegmentFeature(String type, CortexContext cortexContext, boolean useSubTypes) {
		super(type, cortexContext, useSubTypes);
	}

	@Override
	public long getScore(CacheContext cacheContext) {


		if (cacheContext instanceof FeaturedStatementCacheContext && ((FeaturedStatementCacheContext<?>) cacheContext).isLocalized()) {
			return super.getScore(cacheContext) * 2;
		}

		return 0;
	}

}
