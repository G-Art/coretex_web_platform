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

		buildFl(solrQuery);
		buildGroup(solrQuery);
		if (Objects.nonNull(solrSearchRequest)) {
			buildFq(solrQuery);
			buildPaging(solrQuery);
			buildFacet(solrQuery);
			buildSort(solrQuery);
			buildQuery(solrQuery);
		}
		return solrQuery;
	}

	private void buildSort(SolrQuery solrQuery) {
		if (Objects.nonNull(solrSearchRequest.getSortCode())) {
			var solrQueryConfigurationProvider = configurationProviderMap.get(solrSearchRequest.getResultType());
			if (Objects.nonNull(solrQueryConfigurationProvider.solrSortConfigs()) &&
					solrQueryConfigurationProvider.solrSortConfigs().containsKey(solrSearchRequest.getSortCode())) {
				var solrSortConfig = solrQueryConfigurationProvider.solrSortConfigs().get(solrSearchRequest.getSortCode());

				solrSortConfig.getSortingFields()
						.forEach(pair -> {
							var solrDocFieldConfig = pair.getLeft();
							var order = StringUtils.isNotBlank(pair.getRight()) ? pair.getRight() : solrSearchRequest.getSortOrder();
							if (solrDocFieldConfig.isLocalized()) {
								solrQuery.addSort(solrDocFieldConfig.createFullFieldName(solrSearchRequest.getLocale()),
										SolrQuery.ORDER.valueOf(order.toLowerCase()));
							} else {
								solrQuery.addSort(solrDocFieldConfig.createFullFieldName(),
										SolrQuery.ORDER.valueOf(order.toLowerCase()));
							}
						});
				if (solrSortConfig.isIncludeScore()) {
					var scoreOrder = StringUtils.isNotBlank(solrSortConfig.getScoreOrder()) ? solrSortConfig.getScoreOrder() : "desc";
					solrQuery.addSort("score",
							SolrQuery.ORDER.valueOf(scoreOrder));
				}
			}
		}
	}

	private void buildFacet(SolrQuery solrQuery) {
		var solrQueryConfigurationProvider = configurationProviderMap.get(solrSearchRequest.getResultType());
		if (solrQueryConfigurationProvider.facet()) {
			solrQuery.setFacet(true);
			if (solrQueryConfigurationProvider.groupFacet()) {
				solrQuery.set("group.facet", "true");

			}
			solrQuery.setFacetMinCount(solrQueryConfigurationProvider.facetMinCount());
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

	private void buildFq(SolrQuery solrQuery) {
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
				format("%s:%s", facets.createFullFieldName(solrSearchRequest.getLocale()), escapeQueryChars(value)) :
				format("%s:%s", facets.createFullFieldName(), escapeQueryChars(value));
	}

	private void buildPaging(SolrQuery solrQuery) {
		solrQuery.setStart(solrSearchRequest.getCount() * solrSearchRequest.getPage());
		solrQuery.setRows(solrSearchRequest.getCount());
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
		if (StringUtils.isNotBlank(solrSearchRequest.getSearchText())) {
			solrQuery.setQuery(createQuery(queryType == QueryType.WITH_BOOST));
		} else {
			solrQuery.setQuery("*:*");
		}
	}

	private String createQuery(boolean boost) {
		var solrQueryConfigurationProvider = configurationProviderMap.get(solrSearchRequest.getResultType());
		StringBuilder stringBuilder = new StringBuilder();

		solrQueryConfigurationProvider.solrDocFieldConfigs()
				.stream()
				.filter(SolrDocFieldConfig::isInSearch)
				.forEach(solrDocFieldConfig -> {

					String fieldName;
					if (solrDocFieldConfig.isLocalized()) {
						fieldName = solrDocFieldConfig.createFullFieldName(solrSearchRequest.getLocale());
					} else {
						fieldName = solrDocFieldConfig.createFullFieldName();
					}

					if (solrDocFieldConfig.isQuery()) {
						if (stringBuilder.length() > 0) {
							stringBuilder.append(" OR ");
						}
						addQuery(stringBuilder, fieldName, solrDocFieldConfig, solrSearchRequest);
					}
					if (solrDocFieldConfig.isPhraseQuery()) {
						if (stringBuilder.length() > 0) {
							stringBuilder.append(" OR ");
						}
						addPhraseQuery(stringBuilder, fieldName, solrDocFieldConfig, solrSearchRequest);
					}
					if (solrDocFieldConfig.isWildcardQuery()) {
						if (stringBuilder.length() > 0) {
							stringBuilder.append(" OR ");
						}
						addWildcardQuery(stringBuilder, fieldName, solrDocFieldConfig, solrSearchRequest);
					}
					if (solrDocFieldConfig.isFuzzyQuery()) {
						if (stringBuilder.length() > 0) {
							stringBuilder.append(" OR ");
						}
						addFuzzyQuery(stringBuilder, fieldName, solrDocFieldConfig, solrSearchRequest);
					}
				});
		return stringBuilder.toString();
	}

	private void addFuzzyQuery(StringBuilder stringBuilder, String fieldName, SolrDocFieldConfig solrDocFieldConfig, SolrSearchRequest<?> solrSearchRequest) {
		if (solrDocFieldConfig.getFuzzyQueryBoost() > 1) {
			stringBuilder.append(String.format("(%s:%s~%s^%.1f)", fieldName, solrSearchRequest.getSearchText(), solrDocFieldConfig.getFuzzyQueryFuzziness(), solrDocFieldConfig.getFuzzyQueryBoost()));
		} else {
			stringBuilder.append(String.format("(%s:%s~%s)", fieldName, solrSearchRequest.getSearchText(), solrDocFieldConfig.getFuzzyQueryFuzziness()));
		}
	}

	private void addWildcardQuery(StringBuilder stringBuilder, String fieldName, SolrDocFieldConfig solrDocFieldConfig, SolrSearchRequest<?> solrSearchRequest) {
		var wildcardQueryType = solrDocFieldConfig.getWildcardQueryType();
		if (solrDocFieldConfig.getWildcardQueryBoost() > 1) {
			var query = wildcardQueryType.equals(SolrDocFieldConfig.WildcardQueryType.PREFIX) ? "(%s:*%s^%.1f)" : wildcardQueryType.equals(SolrDocFieldConfig.WildcardQueryType.POSTFIX) ? "(%s:%s*^%.1f)" : "(%s:*%s*^%.1f)";
			stringBuilder.append(String.format(query, fieldName, solrSearchRequest.getSearchText(), solrDocFieldConfig.getWildcardQueryBoost()));
		} else {
			var query = wildcardQueryType.equals(SolrDocFieldConfig.WildcardQueryType.PREFIX) ? "(%s:*%s)" : wildcardQueryType.equals(SolrDocFieldConfig.WildcardQueryType.POSTFIX) ? "(%s:%s*)" : "(%s:*%s*)";
			stringBuilder.append(String.format(query, fieldName, solrSearchRequest.getSearchText()));
		}
	}

	private void addPhraseQuery(StringBuilder stringBuilder, String fieldName, SolrDocFieldConfig solrDocFieldConfig, SolrSearchRequest<?> solrSearchRequest) {
		if (solrDocFieldConfig.getPhraseQueryBoost() > 1) {
			stringBuilder.append(String.format("(%s:\"%s\"~%.1f^%.1f)", fieldName, solrSearchRequest.getSearchText(), solrDocFieldConfig.getPhraseQuerySlop(), solrDocFieldConfig.getPhraseQueryBoost()));
		} else {
			stringBuilder.append(String.format("(%s:\"%s\"~%.1f)", fieldName, solrSearchRequest.getSearchText(), solrDocFieldConfig.getPhraseQuerySlop()));
		}
	}

	private void addQuery(StringBuilder stringBuilder, String fieldName, SolrDocFieldConfig solrDocFieldConfig, SolrSearchRequest<?> solrSearchRequest) {
		if (solrDocFieldConfig.getQueryBoost() > 1) {
			stringBuilder.append(String.format("(%s:%s^%.1f)", fieldName, solrSearchRequest.getSearchText(), solrDocFieldConfig.getQueryBoost()));
		} else {
			stringBuilder.append(String.format("(%s:%s)", fieldName, solrSearchRequest.getSearchText()));
		}
	}

	public enum QueryType {
		WITH_BOOST, WITHOUT_BOOST
	}
}
