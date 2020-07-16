package com.coretex.commerce.search.providers;

import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.coretex.searchengine.solr.client.builders.impl.AbstractSolrInputFieldProvider;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.coretex.commerce.core.utils.ProductUtils.buildProductSmallImageUtils;

public class ProductItemImagesSolrInputFieldProvider extends AbstractSolrInputFieldProvider<ProductItem> {

	public ProductItemImagesSolrInputFieldProvider(SolrDocFieldConfig config) {
		super(config);
	}

	@Override
	public void setSolrInputField(SolrInputDocument document, ProductItem source) {
		Assert.notNull(source, "Source item is null");

		var productImages = getProductImages(source);
		document.addField(createFieldName(), productImages);
	}

	private Collection<String> getProductImages(ProductItem source) {
		if (source instanceof VariantProductItem) {
			if (CollectionUtils.isNotEmpty(source.getImages())) {
				return source.getImages()
						.stream()
						.map(image -> buildProductSmallImageUtils(source.getStore(), source.getCode(), image.getProductImage()))
						.collect(Collectors.toList());
			} else {
				return getProductImages(((VariantProductItem) source).getBaseProduct());
			}
		} else {
			return null;
		}

	}

}
