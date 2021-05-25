package com.coretex.core.activeorm.cache.features;

import com.coretex.core.activeorm.cache.CacheContext;
import com.coretex.core.activeorm.cache.CacheSegmentFeature;
import com.coretex.core.activeorm.cache.impl.FeaturedStatementCacheContext;
import com.coretex.core.general.utils.ItemUtils;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.items.core.MetaTypeItem;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

public class ItemTypeCacheSegmentFeature implements CacheSegmentFeature {

	private final CortexContext cortexContext;

	private int scoreBasis = 1;
	private int scoreMultiplier = 10;

	private final String type;
	private final MetaTypeItem metaType;
	private Collection<MetaTypeItem> subTypes;

	private final boolean useSubTypes;

	public ItemTypeCacheSegmentFeature(String type, CortexContext cortexContext) {
		this(type, cortexContext, true);
	}

	public ItemTypeCacheSegmentFeature(String type, CortexContext cortexContext, boolean useSubTypes) {
		this.type = type;
		this.cortexContext = cortexContext;
		this.metaType = cortexContext.findMetaType(type);
		this.useSubTypes = useSubTypes;
		if (this.useSubTypes) {
			subTypes = ItemUtils.getAllSubtypes(metaType);
		}
	}

	@Override
	public long getScore(CacheContext cacheContext) {

		if (cacheContext instanceof FeaturedStatementCacheContext) {
			var itemsUsed = ((FeaturedStatementCacheContext<?>) cacheContext).getItemsUsed();

			if (useSubTypes) {
				if (CollectionUtils.isNotEmpty(itemsUsed) && itemsUsed.stream().anyMatch(metaTypeItem -> metaTypeItem.equals(metaType)) ||
						CollectionUtils.containsAny(subTypes, itemsUsed)) {
					return scoreBasis * scoreMultiplier;
				}
			} else {
				if (CollectionUtils.isNotEmpty(itemsUsed) && itemsUsed.stream().anyMatch(metaTypeItem -> metaTypeItem.getTypeCode().equals(type))) {
					return scoreBasis * scoreMultiplier;
				}
			}
		}

		return 0;
	}

	public boolean isUseSubTypes() {
		return useSubTypes;
	}

	public int getScoreBasis() {
		return scoreBasis;
	}

	public void setScoreBasis(int scoreBasis) {
		this.scoreBasis = scoreBasis;
	}

	public int getScoreMultiplier() {
		return scoreMultiplier;
	}

	public void setScoreMultiplier(int scoreMultiplier) {
		this.scoreMultiplier = scoreMultiplier;
	}
}
