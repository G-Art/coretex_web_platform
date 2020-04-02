package com.coretex.commerce.admin.controllers;

import com.coretex.commerce.admin.facades.SessionFacade;
import com.coretex.commerce.admin.facades.UserFacade;
import com.coretex.commerce.admin.services.SessionService;
import com.coretex.commerce.data.CurrencyData;
import com.coretex.commerce.data.LocaleData;
import com.coretex.commerce.data.StoreData;
import com.coretex.commerce.data.UserData;
import com.coretex.commerce.facades.StoreFacade;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractController {

	public static final String ERROR_MESSAGE_LEVEL = "ERROR_MESSAGES";
	public static final String WARNING_MESSAGE_LEVEL = "WARNING_MESSAGES";
	public static final String INFO_MESSAGE_LEVEL = "INFO_MESSAGES";
	public static final String REDIRECT_PREFIX = "redirect:";
	public static final String FORWARD_PREFIX = "forward:";
	public static final String ROOT = "/";

	@Resource
	private SessionService sessionService;

	@Resource
	private SessionFacade sessionFacade;

	@Resource
	private UserFacade userFacade;

	@Resource
	private StoreFacade storeFacade;

	protected void addFlashMessage(RedirectAttributes redirectAttributes, String message, String level){
		Map<String, Object> flashAttributes = (Map<String, Object>) redirectAttributes.getFlashAttributes();
		var messageLevel = (List)flashAttributes.computeIfAbsent(level, l -> Lists.newArrayList());
		if(StringUtils.isNotBlank(message)){
			messageLevel.add(message);
		}
	}

	protected void addErrorFlashMessage(RedirectAttributes redirectAttributes, String message){
		addFlashMessage(redirectAttributes, message, ERROR_MESSAGE_LEVEL);
	}

	protected void addWarningFlashMessage(RedirectAttributes redirectAttributes, String message){
		addFlashMessage(redirectAttributes, message, WARNING_MESSAGE_LEVEL);
	}

	protected void addInfoFlashMessage(RedirectAttributes redirectAttributes, String message){
		addFlashMessage(redirectAttributes, message, INFO_MESSAGE_LEVEL);
	}

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


	@ModelAttribute("stores")
	public Collection<StoreData> getStores() {
		return storeFacade.getAll().collect(Collectors.toSet());
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
