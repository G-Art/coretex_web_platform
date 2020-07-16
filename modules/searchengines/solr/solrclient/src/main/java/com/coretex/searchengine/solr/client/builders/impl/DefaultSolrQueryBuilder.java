package com.coretex.searchengine.solr.client.builders.impl;

import com.coretex.searchengine.solr.client.builders.SolrQueryBuilder;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import com.coretex.searchengine.solr.client.providers.SolrQueryConfigurationProvider;
import com.coretex.searchengine.solr.client.search.SolrSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.solr.client.solrj.util.ClientUtils.escapeQueryChars;

public class DefaultSolrQueryBuilder implements SolrQueryBuilder {

	private final Map<Class<?>, SolrQueryConfigurationProvider> configurationProviderMap;

	private boolean includeScore = true;
	private QueryType queryType = QueryType.WITHOUT_BOOST;
	private SolrSearchRequest<?> solrSearchRequest;

	private Map<String, String> facetTagsMap = new HashMap<>();


	public DefaultSolrQueryBuilder(Map<Class<?>, SolrQueryConfigurationProvider> configurationProviderMap) {
		this.configurationProviderMap = configurationProviderMap;
	}

	@Override
	public DefaultSolrQueryBuilder setIncludeScore(boolean includeScore) {
		this.includeScore = includeScore;
		return this;
	}

	@Override
	public DefaultSolrQueryBuilder setQueryType(QueryType queryType) {
		this.queryType = queryType;
		return this;
	}

	@Override
	public DefaultSolrQueryBuilder setSolrSearchRequest(SolrSearchRequest<?> solrSearchRequest) {
		this.solrSearchRequest = solrSearchRequest;
		return this;
	}

	@Override
	public SolrQuery build() {
		SolrQuery solrQuery = new SolrQuery();

		solrQuery.setIncludeScore(includeScore);

		buildQuery(solrQuery);
		buildFl(solrQuery);
		buildFq(solrQuery);
		buildGroup(solrQuery);
		buildPaging(solrQuery);
		buildFacet(solrQuery);

		return solrQuery;
	}

	private void buildFacet(SolrQuery solrQuery) {
		if (Objects.nonNull(solrSearchRequest)) {
			var solrQueryConfigurationProvider = configurationProviderMap.get(solrSearchRequest.getResultType());
			if (solrQueryConfigurationProvider.facet()) {
				solrQuery.setFacet(true);
				if (solrQueryConfigurationProvider.groupFacet()) {
					solrQuery.set("group.facet", "true");
				}
			}
			solrQueryConfigurationProvider.solrDocFieldConfigs().stream()
					.filter(SolrDocFieldConfig::isFacet).forEach(solrDocFieldConfig -> {
				if (solrDocFieldConfig.isLocalized()) {
					if (facetTagsMap.containsKey(solrDocFieldConfig.getName())) {
						solrQuery.addFacetField(format("{!ex=%s}%s", facetTagsMap.get(solrDocFieldConfig.getName()), solrDocFieldConfig.createFullFieldName(solrSearchRequest.getLocale())));
					} else {
						solrQuery.addFacetField(solrDocFieldConfig.createFullFieldName(solrSearchRequest.getLocale()));
					}
				} else {

					if (facetTagsMap.containsKey(solrDocFieldConfig.getName())) {
						solrQuery.addFacetField(format("{!ex=%s}%s", facetTagsMap.get(solrDocFieldConfig.getName()), solrQuery.addFacetField(solrDocFieldConfig.createFullFieldName())));
					} else {
						solrQuery.addFacetField(solrDocFieldConfig.createFullFieldName());
					}
				}
			});
		}
	}

	private void buildFq(SolrQuery solrQuery) {
		if (Objects.nonNull(solrSearchRequest)) {
			var solrQueryConfigurationProvider = configurationProviderMap.get(solrSearchRequest.getResultType());

			solrSearchRequest.getFilters()
					.asMap()
					.forEach((key, value) -> {
						var fieldConfig = solrQueryConfigurationProvider.solrDocFieldConfigsByName(key);
						if (Objects.nonNull(fieldConfig)) {
							addFilter(solrQuery, key, value, fieldConfig);
						}
					});

		}
	}

	private void addFilter(SolrQuery solrQuery, String key, Collection<String> value, SolrDocFieldConfig fieldConfig) {

		if (fieldConfig.isFacet()) {
			facetTagsMap.put(fieldConfig.getName(), "fTag" + facetTagsMap.size() + 1);

			if (value.size() == 1) {
				if (fieldConfig.isLocalized()) {
					solrQuery.addFilterQuery(format("{!tag=%s}%s:%s",
							facetTagsMap.get(fieldConfig.getName()),
							fieldConfig.createFullFieldName(solrSearchRequest.getLocale()),
							escapeQueryChars(value.iterator().next())));
				} else {
					solrQuery.addFilterQuery(format("{!tag=%s}%s:%s",
							facetTagsMap.get(fieldConfig.getName()),
							fieldConfig.createFullFieldName(),
							escapeQueryChars(value.iterator().next())));
				}
			} else {
				var filters = value.stream()
						.map(v -> convertField(key, v, fieldConfig, solrSearchRequest))
						.collect(Collectors.joining(fieldConfig.isMultiValueSelect() ? " OR " : " AND "));
				if (StringUtils.isNotBlank(filters)) {
					solrQuery.addFilterQuery(filters);
				}
			}

		} else {
			if (value.size() == 1) {
				if (fieldConfig.isLocalized()) {
					solrQuery.addFilterQuery(format("%s:%s",
							fieldConfig.createFullFieldName(solrSearchRequest.getLocale()),
							escapeQueryChars(value.iterator().next())));
				} else {
					solrQuery.addFilterQuery(format("%s:%s",
							fieldConfig.createFullFieldName(),
							escapeQueryChars(value.iterator().next())));
				}
			} else {
				var filters = value.stream()
						.map(v -> convertField(key, v, fieldConfig, solrSearchRequest))
						.collect(Collectors.joining(" AND "));
				if (StringUtils.isNotBlank(filters)) {
					solrQuery.addFilterQuery(filters);
				}
			}
		}


	}

	protected String convertField(String key, String value, SolrDocFieldConfig facets, SolrSearchRequest<?> solrSearchRequest) {
		if (facets.isFacet()) {
			return facets.isLocalized() ?
					format("{!tag=%s}%s:%s",
							facetTagsMap.get(facets.getName()), facets.createFullFieldName(solrSearchRequest.getLocale()), escapeQueryChars(value)) :
					format("{!tag=%s}%s:%s",
							facetTagsMap.get(facets.getName()), facets.createFullFieldName(), escapeQueryChars(value));
		}
		return facets.isLocalized() ?
				format("{!tag=%s}%s:%s",
						facetTagsMap.get(facets.getName()), facets.createFullFieldName(solrSearchRequest.getLocale()), escapeQueryChars(value)) :
				format("{!tag=%s}%s:%s",
						facetTagsMap.get(facets.getName()), facets.createFullFieldName(), escapeQueryChars(value));
	}

	private void buildPaging(SolrQuery solrQuery) {
		if (Objects.nonNull(solrSearchRequest)) {
			solrQuery.setStart(solrSearchRequest.getCount() * solrSearchRequest.getPage());
			solrQuery.setRows(solrSearchRequest.getCount());
		}
	}

	private void buildGroup(SolrQuery solrQuery) {
		var solrQueryConfigurationProvider = configurationProviderMap.get(solrSearchRequest.getResultType());
		if (Objects.nonNull(solrQueryConfigurationProvider) && solrQueryConfigurationProvider.grouped()) {
			solrQuery.set("group", "true");
			solrQuery.set("group.field", solrQueryConfigurationProvider.groupField());
			if (solrQueryConfigurationProvider.nGrouped()) {
				solrQuery.set("group.ngroups", "true");
			}
			solrQuery.set("group.limit", solrQueryConfigurationProvider.groupLimit());
			if (solrQueryConfigurationProvider.groupFacet()) {
				solrQuery.set("group.facet", solrQueryConfigurationProvider.groupFacet());
			}
		}

	}

	private void buildFl(SolrQuery solrQuery) {
		if (!includeScore) {
			solrQuery.set("fl", "*");
		}
	}

	private void buildQuery(SolrQuery solrQuery) {
		if (queryType == QueryType.WITHOUT_BOOST) {
			solrQuery.setQuery("*:*");
		}
		if (queryType == QueryType.WITH_BOOST) {
			solrQuery.setQuery("*:*");
		}
	}

	public enum QueryType {
		WITH_BOOST, WITHOUT_BOOST
	}
}
