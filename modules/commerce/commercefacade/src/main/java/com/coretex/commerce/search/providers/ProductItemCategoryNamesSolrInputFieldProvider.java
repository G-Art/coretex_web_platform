package com.coretex.commerce.search.providers;

import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.coretex.searchengine.solr.client.builders.impl.AbstractSolrInputFieldProvider;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import com.google.common.collect.Sets;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ProductItemCategoryNamesSolrInputFieldProvider extends AbstractSolrInputFieldProvider<ProductItem> {

	public ProductItemCategoryNamesSolrInputFieldProvider(SolrDocFieldConfig config) {
		super(config);
	}

	@Override
	public void setSolrInputField(SolrInputDocument document, ProductItem source) {
		Assert.notNull(source, "Source item is null");

		ProductItem baseProduct = getBaseProduct(source);

		var category = baseProduct.getCategory();
		Set<CategoryItem> categoryItemList = Sets.newHashSet();

		categoryItemList.add(category);
		addParentCategory(categoryItemList, category);

		Map<Locale, Set<Object>> result = new HashMap<>();
		categoryItemList.stream()
				.map(CategoryItem::allName)
				.forEach(localeStringMap -> localeStringMap
						.forEach((locale, s) -> {
							result.computeIfAbsent(locale, loc -> Sets.newHashSet(s));
							result.computeIfPresent(locale, (loc, list) -> {
								list.add(s);
								return list;
							});
						}));

		result.forEach((locale, objects) -> document.addField(createFieldName(locale), objects));
	}

	private void addParentCategory(Set<CategoryItem> categoryItemList, CategoryItem category) {
		var parent = category.getParent();
		if (Objects.nonNull(parent)) {
			categoryItemList.add(parent);
			addParentCategory(categoryItemList, parent);
		}
	}

	private ProductItem getBaseProduct(ProductItem source) {
		if (source instanceof VariantProductItem) {
			return getBaseProduct(((VariantProductItem) source).getBaseProduct());
		}
		return source;
	}
}
