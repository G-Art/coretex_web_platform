package com.coretex.shop.admin.controller;

import com.coretex.core.activeorm.dao.LocaleDao;
import com.coretex.items.core.LocaleItem;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public class AbstractController {

	@Resource
	private LocaleDao localeDao;

	@ModelAttribute("locales")
	public List<LocaleItem> getSupportedLocales(){
		return localeDao.find(Map.of(LocaleItem.ACTIVE, Boolean.TRUE));
	}
}
