package com.coretex.commerce.admin.controllers;

import com.coretex.commerce.admin.data.GenericItemData;
import com.coretex.commerce.admin.facades.PageableDataTableFacade;
import com.coretex.items.core.GenericItem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class PageableDataTableAbstractController<D extends GenericItemData> extends AbstractController {

	@RequestMapping(path = "/paginated", method = RequestMethod.GET)
	@ResponseBody
	public  DataTableResults<D> getPageableList(@RequestParam("draw") String draw, @RequestParam("start") Long start, @RequestParam("length") Long length){
		return getPageableFacade().tableResult(draw, start / length, length);
	}

	protected abstract PageableDataTableFacade<? extends GenericItem, D> getPageableFacade();

}
