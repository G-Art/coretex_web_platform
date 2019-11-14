
package com.coretex.shop.store.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.coretex.core.business.services.SessionService;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.store.model.paging.PaginationData;

public abstract class AbstractController {

	@Resource
	private SessionService sessionService;

	protected SessionService getSessionService() {
		return sessionService;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getSessionAttribute(final String key, HttpServletRequest request) {
		return (T) sessionService.getSessionAttribute(key);

	}

	protected void setSessionAttribute(final String key, final Object value, HttpServletRequest request) {
		sessionService.setSessionAttribute(key, value);
	}

	protected void removeAttribute(final String key, HttpServletRequest request) {
		sessionService.removeSessionAttribute(key);
	}

	protected LanguageItem getLanguage(HttpServletRequest request) {
		return (LanguageItem) request.getAttribute(Constants.LANGUAGE);
	}

	protected PaginationData createPaginaionData(final int pageNumber, final int pageSize) {
		final PaginationData paginaionData = new PaginationData(pageSize, pageNumber);

		return paginaionData;
	}

	protected PaginationData calculatePaginaionData(final PaginationData paginationData, final int pageSize, final int resultCount) {

		int currentPage = paginationData.getCurrentPage();


		int count = Math.min((currentPage * pageSize), resultCount);
		paginationData.setCountByPage(count);

		paginationData.setTotalCount(resultCount);
		return paginationData;
	}
}
