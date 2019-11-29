package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.GenericItemData;

public interface PageableDataTableFacade<D extends GenericItemData> {

	DataTableResults<D> tableResult(String draw, long page, Long length);
}
