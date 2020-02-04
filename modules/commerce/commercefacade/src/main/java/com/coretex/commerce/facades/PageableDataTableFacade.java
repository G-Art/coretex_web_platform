package com.coretex.commerce.facades;

import com.coretex.commerce.data.DataTableResults;
import com.coretex.commerce.data.GenericItemData;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.items.core.GenericItem;

import java.util.stream.Collectors;

public interface PageableDataTableFacade<I extends GenericItem, D extends GenericItemData> {

	default DataTableResults<D> tableResult(String draw, long page, Long length) {
		var pageableList = getPageableService().pageableList(length, page);
		var dataTableResults = new DataTableResults<D>();
		dataTableResults.setDraw(draw);
		dataTableResults.setRecordsTotal(String.valueOf(pageableList.getTotalCount()));
		dataTableResults.setRecordsFiltered(String.valueOf(pageableList.getTotalCount()));
		dataTableResults.setListOfDataObjects(pageableList.getResult()
				.stream()
				.map(getDataMapper()::fromItem)
				.collect(Collectors.toList()));
		return dataTableResults;
	}

	PageableService<I> getPageableService();

	GenericDataMapper<I, D> getDataMapper();

}
