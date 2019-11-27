package com.coretex.shop.filter;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.user.UserService;
import com.coretex.core.business.utils.CacheUtils;
import com.coretex.items.commerce_core_model.UserItem;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.LanguageUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class AdminFilter extends HandlerInterceptorAdapter {


	private static final Logger LOGGER = LoggerFactory.getLogger(AdminFilter.class);

	@Resource
	private MerchantStoreService merchantService;

	@Resource
	private UserService userService;

	@Resource
	private LanguageService languageService;

	@Resource
	private CacheUtils cache;

	@Resource
	private LanguageUtils languageUtils;

	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws Exception {

		request.setCharacterEncoding("UTF-8");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) cache.getFromCache("MENUMAP");

		UserItem user = (UserItem) request.getSession().getAttribute(Constants.ADMIN_USER);


		String storeCode = com.coretex.core.business.constants.Constants.DEFAULT_STORE;
		MerchantStoreItem store = (MerchantStoreItem) request.getSession().getAttribute(Constants.ADMIN_STORE);


		String userName = request.getRemoteUser();

		if (userName == null) {//** IMPORTANT FOR SPRING SECURITY **//
			//response.sendRedirect(new StringBuilder().append(request.getContextPath()).append("/").append("/admin").toString());
		} else {

			if (user == null) {
				user = userService.getByUserName(userName);
				request.getSession().setAttribute(Constants.ADMIN_USER, user);
				if (user != null) {
					storeCode = user.getMerchantStore().getCode();
				} else {
					LOGGER.warn("UserItem name not found " + userName);
				}
				store = null;
			}

			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/admin/unauthorized.html");
				return true;
			}

			if (!user.getAdminName().equals(userName)) {
				user = userService.getByUserName(userName);
				if (user != null) {
					storeCode = user.getMerchantStore().getCode();
				} else {
					LOGGER.warn("UserItem name not found " + userName);
				}
				store = null;
			}

		}

		if (store == null) {
			store = merchantService.getByCode(storeCode);
			request.getSession().setAttribute(Constants.ADMIN_STORE, store);
		}
		request.setAttribute(Constants.ADMIN_STORE, store);


		LocaleItem language = languageUtils.getRequestLanguage(request, response);

		//LocaleItem language = (LocaleItem) request.getSession().getAttribute("LANGUAGE");

		if (language == null) {

			//TODO get the Locale from Spring API, is it simply request.getLocale() ???
			//if so then based on the Locale language locale.getLanguage() get the appropriate LocaleItem
			//object as represented below
			if (user != null) {
				language = user.getLanguage();
				if (language == null) {
					language = store.getDefaultLanguage();
				}
			} else {
				language = store.getDefaultLanguage();
			}

			request.getSession().setAttribute("LANGUAGE", language);

		}


		request.setAttribute(Constants.LANGUAGE, language);


		if (menus == null) {
			InputStream in = null;
			ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			try {
				in =
						this.getClass().getClassLoader().getResourceAsStream("admin/menu.json");

				Map<String, Object> data = mapper.readValue(in, Map.class);

				Menu currentMenu = null;

				menus = new LinkedHashMap<String, Menu>();
				List objects = (List) data.get("menus");
				for (Object object : objects) {
					Menu m = getMenu(object);
					menus.put(m.getCode(), m);
				}

				cache.putInCache(menus, "MENUMAP");

			} catch (JsonParseException e) {
				LOGGER.error("Error while creating menu", e);
			} catch (JsonMappingException e) {
				LOGGER.error("Error while creating menu", e);
			} catch (IOException e) {
				LOGGER.error("Error while creating menu", e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception ignore) {
						// TODO: handle exception
					}
				}
			}

		}


		List<Menu> list = new ArrayList<Menu>(menus.values());

		request.setAttribute("MENULIST", list);


		request.setAttribute("MENUMAP", menus);
		response.setCharacterEncoding("UTF-8");

		return true;
	}


	private Menu getMenu(Object object) {

		Map o = (Map) object;
		Map menu = (Map) o.get("menu");

		Menu m = new Menu();
		m.setCode((String) menu.get("code"));


		m.setUrl((String) menu.get("url"));
		m.setIcon((String) menu.get("icon"));
		m.setRole((String) menu.get("role"));

		List menus = (List) menu.get("menus");
		if (menus != null) {
			for (Object oo : menus) {

				Menu mm = getMenu(oo);
				m.getMenus().add(mm);
			}

		}

		return m;

	}

}
