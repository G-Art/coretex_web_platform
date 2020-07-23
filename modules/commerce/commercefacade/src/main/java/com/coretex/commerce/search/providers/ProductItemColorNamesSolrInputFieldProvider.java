package com.coretex.commerce.search.providers;

import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.StyleDescriptionItem;
import com.coretex.items.cx_core.StyleVariantProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.coretex.searchengine.solr.client.builders.impl.AbstractSolrInputFieldProvider;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ProductItemColorNamesSolrInputFieldProvider extends AbstractSolrInputFieldProvider<ProductItem> {

	public ProductItemColorNamesSolrInputFieldProvider(SolrDocFieldConfig config) {
		super(config);
	}

	@Override
	public void setSolrInputField(SolrInputDocument document, ProductItem source) {
		Assert.notNull(source, "Source item is null");

		ProductItem baseProduct = getBaseProduct(source);

		var variants = baseProduct.getVariants();
		Set<StyleDescriptionItem> styleDescriptionItems = Sets.newHashSet();
		if (CollectionUtils.isNotEmpty(variants)) {
			variants.forEach(variant -> {

				if (variant instanceof StyleVariantProductItem) {
					styleDescriptionItems.add(((StyleVariantProductItem) variant).getStyle());
				}

			});
		}

		Map<Locale, Set<Object>> result = new HashMap<>();
		styleDescriptionItems.stream()
				.map(StyleDescriptionItem::allStyleName)
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

	private ProductItem getBaseProduct(ProductItem source) {
		if (source instanceof VariantProductItem) {
			return getBaseProduct(((VariantProductItem) source).getBaseProduct());
		}
		return source;
	}
}
