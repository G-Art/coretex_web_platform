package com.coretex.shop.store.controller.category.converter;

import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.model.catalog.category.Category;
import com.coretex.shop.model.catalog.category.CategoryDescription;
import com.coretex.shop.model.catalog.category.ReadableCategory;
import com.coretex.shop.store.controller.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReadableCategoryConverter implements Converter<CategoryItem, ReadableCategory> {

	@Override
	public ReadableCategory convert(CategoryItem source, MerchantStoreItem store, LanguageItem language) {
		ReadableCategory target = new ReadableCategory();


		Optional<Category> parentCategory =
				createParentCategory(source);
		parentCategory.ifPresent(target::setParent);

		Optional.ofNullable(source.getDepth()).ifPresent(target::setDepth);

		target.setDescription(getCategoryDescription(source).get());
		target.setLineage(source.getLineage());
		target.setCode(source.getCode());
		target.setUuid(source.getUuid());
		target.setSortOrder(source.getSortOrder());
		target.setVisible(source.getVisible());
		target.setFeatured(source.getFeatured());
		return target;
	}

	private Optional<CategoryDescription> getCategoryDescription(CategoryItem source) {

		if (source != null) {
			return Optional.of(convertDescription(source));
		} else {
			return Optional.empty();
		}
	}

	private CategoryDescription convertDescription(CategoryItem source) {
		final CategoryDescription desc = new CategoryDescription();

		desc.setFriendlyUrl(source.getSeUrl());
		desc.setName(source.getName());
		desc.setDescription(source.getName());
		desc.setKeyWords(source.getMetatagKeywords());
		desc.setHighlights(source.getCategoryHighlight());
		desc.setTitle(source.getMetatagTitle());
		desc.setMetaDescription(source.getMetatagDescription());
		return desc;
	}

	private Optional<Category> createParentCategory(
			CategoryItem source) {
		return Optional.ofNullable(source.getParent())
				.map(
						parentValue -> {
							final Category parent =
									new Category();
							parent.setCode(source.getParent().getCode());
							parent.setUuid(source.getParent().getUuid());
							return parent;
						});
	}
}
