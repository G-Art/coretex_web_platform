package com.coretex.core.activeorm.dao;

import com.coretex.core.activeorm.dao.SortParameters.SortOrder;
import com.coretex.core.activeorm.exceptions.AmbiguousResultException;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchResult;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.items.core.GenericItem;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DefaultGenericDao <I extends GenericItem> implements Dao<I> {

	private final String typecode;

	@Resource
	private SearchService searchService;

	@Resource
	private ItemService itemService;

	public DefaultGenericDao(String typecode) {
		this.typecode = typecode;
	}

	@Override
	public void save(I item) {
		itemService.save(item);
	}

	@Override
	public void delete(I item) {
		itemService.delete(item);
	}

	@Override
	public Long count() {
		SelectOperationSpec<Map<String,Long>> query = this.createCountSearchQuery();
		var result = this.getSearchService().search(query).getResult();
		if(CollectionUtils.isEmpty(result)){
			return 0L;
		}
		var resultMap = result.iterator().next();
		if(!resultMap.containsKey("count")){
			return 0L;
		}
		return result.iterator().next().get("count");
	}

	@Override
	public List<I> find() {
		SelectOperationSpec<I> query = this.createSearchQuery();
		return this.getSearchService().search(query).getResult();
	}

	@Override
	public I find(UUID uuid) {
		return findSingle(Map.of("uuid", uuid), false);
	}

	@Override
	public I findSingle(Map<String, ?> params, boolean throwAmbiguousException){
		var result = find(params);
		if(CollectionUtils.isNotEmpty(result) && result.size()>1 && throwAmbiguousException){
			throw new AmbiguousResultException(String.format("Result contain more than one result (result count: %s)", result.size()));
		}
		return CollectionUtils.isNotEmpty(result) ? result.iterator().next() : null;
	}

	@Override
	public List<I> find(String query) {
		SearchService ss = this.getSearchService();
		SearchResult<I> searchResult = ss.search(query);
		return searchResult.getResult();
	}

	@Override
	public List<I> find(String query, Map<String, Object> params) {
		SearchService ss = this.getSearchService();
		SearchResult<I> searchResult = ss.search(query, params);
		return searchResult.getResult();
	}

	@Override
	public List<I> find(Map<String, ?> params) {
		SelectOperationSpec<I> query = this.createSearchQuery(params);
		SearchService ss = this.getSearchService();
		SearchResult<I> searchResult = ss.search(query);
		return searchResult.getResult();
	}

	@Override
	public List<I> find(SortParameters sortParameters) {
		SelectOperationSpec<I> query = this.createSearchQuery(sortParameters);
		return this.getSearchService().search(query).getResult();
	}

	@Override
	public List<I> find(Map<String, ?> params, SortParameters sortParameters) {
		SelectOperationSpec<I> query = this.createSearchQuery(params, sortParameters, -1);
		return this.getSearchService().search(query).getResult();
	}

	@Override
	public List<I> find(Map<String, ?> params, SortParameters sortParameters, int count) {
		SelectOperationSpec<I> query = this.createSearchQuery(params, sortParameters, count);
		return this.getSearchService().search(query).getResult();
	}

	@Override
	public List<I> find(Map<String, ?> params, SortParameters sortParameters, int count, int start) {
		SelectOperationSpec<I> query = this.createSearchQuery(params, sortParameters, count, start);
		return this.getSearchService().search(query).getResult();
	}

	private SelectOperationSpec<I> createSearchQuery() {
		StringBuilder builder = this.createQueryString();
		return new SelectOperationSpec<>(builder.toString());
	}
	private SelectOperationSpec<Map<String, Long>> createCountSearchQuery() {
		StringBuilder builder = this.createCountQueryString();
		return new SelectOperationSpec<>(builder.toString());
	}

	private SelectOperationSpec<I> createSearchQuery(Map<String, ?> params) {
		StringBuilder builder = this.createQueryString();
		this.appendWhereClausesToBuilder(builder, params);
		SelectOperationSpec<I> query = new SelectOperationSpec<>(builder.toString());
		if (params != null && !params.isEmpty()) {
			query.addQueryParameters(params);
		}

		return query;
	}

	private SelectOperationSpec<I> createSearchQuery(SortParameters sortParameters) {
		StringBuilder builder = this.createQueryString();
		this.appendOrderByClausesToBuilder(builder, sortParameters);
		return new SelectOperationSpec<>(builder.toString());
	}

	private SelectOperationSpec<I> createSearchQuery(Map<String, ?> params, SortParameters sortParameters, int count) {
		StringBuilder builder = this.createQueryString();
		this.appendWhereClausesToBuilder(builder, params);
		this.appendOrderByClausesToBuilder(builder, sortParameters);
		SelectOperationSpec<I> query = new SelectOperationSpec<>(builder.toString());
		if(count > 0){
			query.setCount(count);
		}

		if (params != null && !params.isEmpty()) {
			query.addQueryParameters(params);
		}

		return query;
	}

	private SelectOperationSpec<I> createSearchQuery(Map<String, ?> params, SortParameters sortParameters, int count, int start) {
		StringBuilder builder = this.createQueryString();
		this.appendWhereClausesToBuilder(builder, params);
		this.appendOrderByClausesToBuilder(builder, sortParameters);
		SelectOperationSpec<I> query = new SelectOperationSpec<>(builder.toString());
		query.setCount(count);
		query.setStart(start);

		if (params != null && !params.isEmpty()) {
			query.addQueryParameters(params);
		}

		return query;
	}

	private StringBuilder createQueryString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * ");
		builder.append("FROM \"").append(this.typecode).append("\" AS c ");
		return builder;
	}

	private StringBuilder createCountQueryString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT count(c.uuid) ");
		builder.append("FROM \"").append(this.typecode).append("\" AS c ");
		return builder;
	}

	private void appendWhereClausesToBuilder(StringBuilder builder, Map<String, ?> params) {
		if (params != null && !params.isEmpty()) {
			builder.append("WHERE ");
			boolean firstParam = true;

			for (Iterator var5 = params.keySet().iterator(); var5.hasNext(); firstParam = false) {
				String paramName = (String) var5.next();
				if (!firstParam) {
					builder.append("AND ");
				}

				builder.append(" c.").append(paramName).append("=:").append(paramName).append(" ");
			}
		}

	}

	private void appendOrderByClausesToBuilder(StringBuilder builder, SortParameters sortParameters) {
		if (sortParameters != null && !sortParameters.isEmpty()) {
			builder.append("ORDER BY ");
			boolean firstParam = true;
			Map<String, SortOrder> sortParams = sortParameters.getSortParameters();

			for (Iterator var6 = sortParams.keySet().iterator(); var6.hasNext(); firstParam = false) {
				String name = (String) var6.next();
				if (!firstParam) {
					builder.append(", ");
				}

				builder.append("c.").append(name).append(" ").append(sortParams.get(name));
			}
		}

	}

	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

}
