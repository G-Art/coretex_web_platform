package com.coretex.commerce.admin.controllers;

import com.coretex.commerce.data.GenericItemData;
import com.coretex.commerce.facades.PageableDataTableFacade;
import com.coretex.commerce.data.DataTableResults;
import com.coretex.items.core.GenericItem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class PageableDataTableAbstractController<D extends GenericItemData> extends AbstractController {

	@RequestMapping(path = "/paginated", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public DataTableResults<D> getPageableList(@RequestParam("draw") String draw, @RequestParam("start") Long start, @RequestParam("length") Long length){
		return getPageableFacade().tableResult(draw, start / length, length);
	}

	protected abstract PageableDataTableFacade<? extends GenericItem, D> getPageableFacade();

}
