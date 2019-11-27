package com.coretex.shop.populator.catalog;

import com.coretex.items.commerce_core_model.CategoryItem;
import org.apache.commons.lang3.Validate;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.catalog.category.ReadableCategory;

public class ReadableCategoryPopulator extends
		AbstractDataPopulator<CategoryItem, ReadableCategory> {

	@Override
	public ReadableCategory populate(final CategoryItem source,
									 final ReadableCategory target,
									 final MerchantStoreItem store,
									 final LocaleItem language) throws ConversionException {

		Validate.notNull(source, "CategoryItem must not be null");

		target.setLineage(source.getLineage());

		final com.coretex.shop.model.catalog.category.CategoryDescription desc = new com.coretex.shop.model.catalog.category.CategoryDescription();
		desc.setFriendlyUrl(source.getSeUrl());
		desc.setName(source.getName());
		desc.setUuid(source.getUuid());
		desc.setDescription(source.getName());
		desc.setKeyWords(source.getMetatagKeywords());
		desc.setHighlights(source.getCategoryHighlight());
		desc.setTitle(source.getMetatagTitle());
		desc.setMetaDescription(source.getMetatagDescription());

		target.setDescription(desc);

		if (source.getParent() != null) {
			final com.coretex.shop.model.catalog.category.Category parent = new com.coretex.shop.model.catalog.category.Category();
			parent.setCode(source.getParent().getCode());
			parent.setUuid(source.getParent().getUuid());
			target.setParent(parent);
		}

		target.setCode(source.getCode());
		target.setUuid(source.getUuid());
		if (source.getDepth() != null) {
			target.setDepth(source.getDepth());
		}
		target.setSortOrder(source.getSortOrder());
		target.setVisible(source.getVisible());
		target.setFeatured(source.getFeatured());

		return target;

	}

	@Override
	protected ReadableCategory createTarget() {
		return null;
	}

}
