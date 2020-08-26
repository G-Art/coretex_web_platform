package com.coretex.commerce.search.providers;

import com.coretex.commerce.core.utils.ProductUtils;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.searchengine.solr.client.builders.impl.AbstractSolrInputFieldProvider;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.util.Assert;

import java.math.RoundingMode;

public class ProductItemPriceSolrInputFieldProvider extends AbstractSolrInputFieldProvider<ProductItem> {

	public ProductItemPriceSolrInputFieldProvider(SolrDocFieldConfig config) {
		super(config);
	}

	@Override
	public void setSolrInputField(SolrInputDocument document, ProductItem source) {
		Assert.notNull(source, "Source item is null");

		var productImages = getProductPrice(source);
		document.addField(createFieldName(), productImages);
	}

	private Double getProductPrice(ProductItem source) {
		var availabilities = ProductUtils.getAvailabilities(source);
		return availabilities
				.stream()
				.findFirst()
				.map(productAvailabilityItem ->
						productAvailabilityItem.getPrices()
								.stream()
								.findFirst()
								.map(productPriceItem -> productPriceItem.getProductPriceAmount()
										.setScale(2, RoundingMode.HALF_UP)
										.doubleValue())
								.orElse(0.00d))
				.orElse(0.00d);
	}

}
