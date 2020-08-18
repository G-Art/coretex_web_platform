package com.coretex.commerce.search.converters;

import com.coretex.commerce.data.ImageData;
import com.coretex.commerce.data.ShortProductData;
import com.coretex.commerce.data.ShortVariantProductData;
import com.coretex.searchengine.solr.client.SolrResponseDataConverter;
import com.coretex.searchengine.solr.client.providers.SolrQueryConfigurationProvider;
import com.coretex.searchengine.solr.client.search.SolrSearchRequest;
import com.coretex.searchengine.solr.client.search.SolrSearchResponse;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DefaultSolrResponseProductDataConverter implements SolrResponseDataConverter {
	@Override
	public <R> void convert(QueryResponse qr,
							SolrSearchResponse<R> response,
							SolrQueryConfigurationProvider solrQueryConfigurationProvider) {

		if (Objects.nonNull(qr) && Objects.nonNull(qr.getGroupResponse())) {
			qr.getGroupResponse().getValues()
					.forEach(groupCommand -> {
						response.setTotalCount(Long.valueOf(groupCommand.getNGroups()));
						response.setTotalPages((int) Math.ceil(((double) response.getTotalCount())
								/ response.getRequest().getCount()));
						groupCommand.getValues()
								.forEach(group -> {
									ShortProductData shortProductData = convertGroup(group, response, solrQueryConfigurationProvider);
									response.getResult().add((R) shortProductData);
								});
					});
		}

	}

	private <R> ShortProductData convertGroup(Group group, SolrSearchResponse<R> response,
											  SolrQueryConfigurationProvider solrQueryConfigurationProvider) {
		var shortProductData = new ShortProductData();
		var groupValue = group.getGroupValue();
		shortProductData.setCode(groupValue);
		var first = IteratorUtils.first(group.getResult().iterator());
		shortProductData.setDefaultVariantCode(getFieldValue(first, "code", solrQueryConfigurationProvider, response.getRequest()));
		group.getResult()
				.stream()
				.collect(Collectors.groupingBy(doc -> this.<String>getFieldValue(doc, "parentProduct", solrQueryConfigurationProvider, response.getRequest()),
						Collectors.toSet()))
				.forEach((key, solrDocuments) -> {

					ShortVariantProductData shortVariantProductData = convertStyleVariant(key,
							solrDocuments,
							response,
							solrQueryConfigurationProvider,
							doc -> {
								shortProductData.setUuid(UUID.fromString(getFieldValue(doc, "baseProduct", solrQueryConfigurationProvider, response.getRequest())));
								shortProductData.setName(getFieldValue(doc, "name", solrQueryConfigurationProvider, response.getRequest()));
							});

					if (CollectionUtils.isEmpty(shortProductData.getVariants())) {
						shortProductData.setVariants(Sets.newHashSet());
					}
					shortProductData.getVariants().add(shortVariantProductData);

				});
		return shortProductData;
	}

	protected <R> ShortVariantProductData convertStyleVariant(String uuid,
															  Set<SolrDocument> solrDocuments,
															  SolrSearchResponse<R> response,
															  SolrQueryConfigurationProvider solrQueryConfigurationProvider,
															  Consumer<SolrDocument> hook) {
		ShortVariantProductData shortVariantProductData = new ShortVariantProductData();
		shortVariantProductData.setUuid(UUID.fromString(uuid));

		solrDocuments.forEach(solDoc -> {
			shortVariantProductData.setCode(getFieldValue(solDoc, "parentProductCode", solrQueryConfigurationProvider, response.getRequest()));
			shortVariantProductData.setName(getFieldValue(solDoc, "name", solrQueryConfigurationProvider, response.getRequest()));
			shortVariantProductData.setColorCssCode(getFieldValue(solDoc, "colorCode", solrQueryConfigurationProvider, response.getRequest()));

			shortVariantProductData.setImages(this.<String>getFieldValues(solDoc, "images", solrQueryConfigurationProvider, response.getRequest()).stream()
					.map(ImageData::new)
					.toArray(ImageData[]::new));

			ShortVariantProductData sizeShortVariantProductData = convertSizeVariant(solDoc, response, solrQueryConfigurationProvider, doc -> {
				if (Objects.nonNull(hook)) {
					hook.accept(solDoc);
				}
			});

			if (CollectionUtils.isEmpty(shortVariantProductData.getVariants())) {
				shortVariantProductData.setVariants(Sets.newHashSet());
			}

			shortVariantProductData.getVariants().add(sizeShortVariantProductData);
		});
		return shortVariantProductData;
	}

	protected <R> ShortVariantProductData convertSizeVariant(SolrDocument solDoc, SolrSearchResponse<R> response, SolrQueryConfigurationProvider solrQueryConfigurationProvider, Consumer<SolrDocument> hook) {
		ShortVariantProductData sizeShortVariantProductData = new ShortVariantProductData();
		sizeShortVariantProductData.setUuid(UUID.fromString((String) solDoc.getFieldValue("uuid")));
		sizeShortVariantProductData.setCode(getFieldValue(solDoc, "code", solrQueryConfigurationProvider, response.getRequest()));
		sizeShortVariantProductData.setName(getFieldValue(solDoc, "name", solrQueryConfigurationProvider, response.getRequest()));
		sizeShortVariantProductData.setDescription(getFieldValue(solDoc, "description", solrQueryConfigurationProvider, response.getRequest()));
		sizeShortVariantProductData.setSize(getFieldValue(solDoc, "size", solrQueryConfigurationProvider, response.getRequest()));
		sizeShortVariantProductData.setColorCssCode(getFieldValue(solDoc, "colorCode", solrQueryConfigurationProvider, response.getRequest()));
		sizeShortVariantProductData.setPrice(this.<Double>getFieldValue(solDoc, "price", solrQueryConfigurationProvider, response.getRequest()).toString());

		if (Objects.nonNull(hook)) {
			hook.accept(solDoc);
		}
		return sizeShortVariantProductData;
	}

	protected <T> T getFieldValue(SolrDocument solrDocument, String fieldName, SolrQueryConfigurationProvider solrQueryConfigurationProvider, SolrSearchRequest<?> request) {
		var solrDocFieldConfig = solrQueryConfigurationProvider.solrDocFieldConfigsByName(fieldName);
		if (solrDocFieldConfig.isLocalized()) {
			return (T) solrDocument.getFieldValue(solrDocFieldConfig.createFullFieldName(request.getLocale()));
		}
		return (T) solrDocument.getFieldValue(solrDocFieldConfig.createFullFieldName());
	}

	protected <T> Collection<T> getFieldValues(SolrDocument solrDocument, String fieldName, SolrQueryConfigurationProvider solrQueryConfigurationProvider, SolrSearchRequest<?> request) {
		var solrDocFieldConfig = solrQueryConfigurationProvider.solrDocFieldConfigsByName(fieldName);
		if (solrDocFieldConfig.isLocalized()) {
			return (Collection<T>) solrDocument.getFieldValues(solrDocFieldConfig.createFullFieldName(request.getLocale()));
		}
		return (Collection<T>) solrDocument.getFieldValues(solrDocFieldConfig.createFullFieldName());
	}
}
