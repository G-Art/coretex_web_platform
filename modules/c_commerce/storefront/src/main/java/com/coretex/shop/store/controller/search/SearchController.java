package com.coretex.shop.store.controller.search;

import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.search.SearchService;
import com.coretex.core.model.search.SearchKeywords;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.SearchProductList;
import com.coretex.shop.model.catalog.SearchProductRequest;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.store.controller.search.facade.SearchFacade;
import com.coretex.shop.store.model.search.AutoCompleteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class SearchController {

	@Resource
	private MerchantStoreService merchantStoreService;

	@Resource
	private SearchService searchService;

	@Resource
	private SearchFacade searchFacade;


	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

	private final static int AUTOCOMPLETE_ENTRIES_COUNT = 15;


	/**
	 * Retrieves a list of keywords for a given series of character typed by the end user
	 * This is used for auto complete on search input field
	 *
	 * @param json
	 * @param store
	 * @param language
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/services/public/search/{store}/{language}/autocomplete.json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String autocomplete(@RequestParam("q") String query, @PathVariable String store, @PathVariable final String language, Model model, HttpServletRequest request, HttpServletResponse response) {

		MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		if (merchantStore != null) {
			if (!merchantStore.getCode().equals(store)) {
				merchantStore = null; //reset for the current request
			}
		}

		try {

			if (merchantStore == null) {
				merchantStore = merchantStoreService.getByCode(store);
			}

			if (merchantStore == null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
				return null;
			}

			AutoCompleteRequest req = new AutoCompleteRequest(store, language);
			/** formatted toJSONString because of te specific field names required in the UI **/
			SearchKeywords keywords = searchService.searchForKeywords(req.getCollectionName(), req.toJSONString(query), AUTOCOMPLETE_ENTRIES_COUNT);
			return keywords.toJSONString();


		} catch (Exception e) {
			LOGGER.error("Exception while autocomplete " + e);
		}

		return null;

	}


	/**
	 * Displays the search result page
	 *
	 * @param store
	 * @param language
	 * @param model
	 * @return
	 * @throws Exception
	 */
	//@RequestMapping(value="/services/public/search/{store}/{language}/{start}/{max}/search.json", method=RequestMethod.POST)
	@RequestMapping(value = "/services/public/search.json", method = RequestMethod.POST)
	@ResponseBody
	public SearchProductList search(
			@RequestBody SearchProductRequest searchRequest,
			Model model,
			LocaleItem language,
			MerchantStoreItem store) {


		//SearchProductList returnList = new SearchProductList();
		//MerchantStoreItem merchantStore = (MerchantStoreItem)request.getAttribute(Constants.MERCHANT_STORE);

		return searchFacade.search(store, language, searchRequest);
/*		
		String json = null;
		
		try {
			
			StringWriter writer = new StringWriter();
			IOUtils.copy(request.getInputStream(), writer, "UTF-8");
			json = writer.toString();
			
			Map<String,LocaleItem> langs = languageService.getLanguagesMap();
			
			if(merchantStore!=null) {
				if(!merchantStore.getCode().equals(store)) {
					merchantStore = null; //reset for the current request
				}
			}
			
			if(merchantStore== null) {
				merchantStore = merchantStoreService.getByCode(store);
			}
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
				return null;
			}
			
			LocaleItem l = langs.get(language);
			if(l==null) {
				l = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
			}

			SearchResponse resp = searchService.search(merchantStore, language, json, max, start);
			return searchFacade.copySearchResponse(resp, merchantStore, start, max, l);

		} catch (Exception e) {
			LOGGER.error("Exception occured while querying " + json,e);
		}
		

		
		return returnList;*/

	}

	/**
	 * Displays the search page after a search query post
	 *
	 * @param query
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/shop/search/search.html"}, method = RequestMethod.POST)
	public String displaySearch(@RequestParam("q") String query, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		String q = request.getParameter("q");

		model.addAttribute("q", q);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Search.search).append(".").append(store.getStoreTemplate());
		return template.toString();
	}


}
