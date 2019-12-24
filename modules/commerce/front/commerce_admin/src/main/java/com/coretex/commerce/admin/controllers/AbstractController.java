package com.coretex.commerce.admin.controllers;

import com.coretex.commerce.data.CurrencyData;
import com.coretex.commerce.data.LocaleData;
import com.coretex.commerce.data.UserData;
import com.coretex.commerce.admin.facades.SessionFacade;
import com.coretex.commerce.admin.facades.UserFacade;
import com.coretex.core.business.services.SessionService;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.Collection;

public class AbstractController {

	public static final String REDIRECT_PREFIX = "redirect:";
	public static final String FORWARD_PREFIX = "forward:";
	public static final String ROOT = "/";

	@Resource
	private SessionService sessionService;

	@Resource
	private SessionFacade sessionFacade;

	@Resource
	private UserFacade userFacade;

	protected String redirect(String path){
		return String.format("%s%s", REDIRECT_PREFIX, path);
	}

	protected String forward(String path){
		return String.format("%s%s", FORWARD_PREFIX, path);
	}

	protected SessionService getSessionService() {
		return sessionService;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getSessionAttribute(final String key) {
		return (T) sessionService.getSessionAttribute(key);
	}

	protected void setSessionAttribute(final String key, final Object value) {
		sessionService.setSessionAttribute(key, value);
	}

	protected void removeAttribute(final String key) {
		sessionService.removeSessionAttribute(key);
	}

	@ModelAttribute("languages")
	public Collection<LocaleData> getLanguages() {
		return sessionFacade.getAllLanguages();
	}

	@ModelAttribute("currencies")
	public Collection<CurrencyData> getCurrencies() {
		return sessionFacade.getAllCurrencies();
	}

	@ModelAttribute("currentLanguage")
	public LocaleData getCurrentLanguage() {
		return sessionFacade.getCurrentLanguage();
	}

	@ModelAttribute("currentCurrency")
	public CurrencyData getCurrentCurrency() {
		return sessionFacade.getCurrentCurrency();
	}

	@ModelAttribute("currentUser")
	public UserData getCurrentUser(){
		return userFacade.getCurrentUser();
	}

}
