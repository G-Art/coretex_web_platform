package com.coretex.data;

import com.coretex.converter.ItemConverter;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.core.GenericItem;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;

public class SearchResultDTO {
	private long count;
	private long totalCount;
	private long page;
	private long totalPages;
	private String transformedQuery;
	private Collection<SearchResultRowDTO> maps ;
	private Collection<SearchResultRowDTO> rows ;

	public SearchResultDTO(PageableSearchResult searchResult, ItemConverter itemConverter) {
		this.count = searchResult.getCount();
		this.totalCount = searchResult.getTotalCount();
		this.page=searchResult.getPage();
		this.totalPages = searchResult.getTotalPages();
		this.transformedQuery = searchResult.getTransformedQuery();

		this.maps = Lists.newArrayList();
		this.rows = Lists.newArrayList();

		searchResult.getResult().forEach(item -> {
			if(item instanceof GenericItem){
				maps.add(new SearchResultRowDTO(itemConverter.convertToMap((GenericItem) item), DataType.MAP)) ;
			} else {
				rows.add(new SearchResultRowDTO(item, DataType.STRING));
			}
		});

	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Collection<SearchResultRowDTO> getRows() {
		return Collections.unmodifiableCollection(rows);
	}

	public Collection<SearchResultRowDTO> getMaps() {
		return Collections.unmodifiableCollection(maps);
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public String getTransformedQuery() {
		return transformedQuery;
	}

	public void setTransformedQuery(String transformedQuery) {
		this.transformedQuery = transformedQuery;
	}
}
