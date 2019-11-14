package com.coretex.data;

import com.coretex.converter.ItemConverter;
import com.coretex.core.activeorm.services.SearchResult;
import com.coretex.items.core.GenericItem;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;

public class SearchResultDTO {
	private long count;
	private Collection<SearchResultRowDTO> maps ;
	private Collection<SearchResultRowDTO> rows ;

	public SearchResultDTO(SearchResult searchResult, ItemConverter itemConverter) {
		this.count = searchResult.getCount();
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
}
