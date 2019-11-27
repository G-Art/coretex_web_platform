package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MinimalOrderData;

public interface OrderFacade {
	DataTableResults<MinimalOrderData> tableResult(String draw, long page, Long length);
}
