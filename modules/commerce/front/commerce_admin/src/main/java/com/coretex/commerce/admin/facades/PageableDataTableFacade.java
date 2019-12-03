package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.GenericItemData;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.core.business.services.common.generic.PageableEntityService;
import com.coretex.items.core.GenericItem;

import java.util.stream.Collectors;

public interface PageableDataTableFacade<I extends GenericItem, D extends GenericItemData> {

	default DataTableResults<D> tableResult(String draw, long page, Long length) {
		var pageableList = getPageableService().pageableList(length, page);
		var dataTableResults = new DataTableResults<D>();
		dataTableResults.setDraw(draw);
		dataTableResults.setRecordsTotal(String.valueOf(pageableList.getTotalCount()));
		dataTableResults.setRecordsFiltered(String.valueOf(pageableList.getCount()));
		dataTableResults.setListOfDataObjects(pageableList.getResult()
				.stream()
				.map(getDataMapper()::fromItem)
				.collect(Collectors.toList()));
		return dataTableResults;
	}

	PageableEntityService<I> getPageableService();

	GenericDataMapper<I, D> getDataMapper();

}
