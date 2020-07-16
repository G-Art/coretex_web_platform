package com.coretex.commerce.search.providers;

import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.coretex.searchengine.solr.client.builders.impl.AbstractSolrInputFieldProvider;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import com.google.common.collect.Lists;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductItemCategoryCodesSolrInputFieldProvider extends AbstractSolrInputFieldProvider<ProductItem> {

	public ProductItemCategoryCodesSolrInputFieldProvider(SolrDocFieldConfig config) {
		super(config);
	}

	@Override
	public void setSolrInputField(SolrInputDocument document, ProductItem source) {
		Assert.notNull(source, "Source item is null");

		ProductItem baseProduct = getBaseProduct(source);

		var category = baseProduct.getCategory();
		List<CategoryItem> categoryItemList = Lists.newArrayList();

		categoryItemList.add(category);
		addParentCategory(categoryItemList, category);

		document.addField(createFieldName(), List.of(categoryItemList.stream()
				.map(CategoryItem::getCode)
				.collect(Collectors.toList())));
	}

	private void addParentCategory(List<CategoryItem> categoryItemList, CategoryItem category) {
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
