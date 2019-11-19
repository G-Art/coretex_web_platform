package com.coretex.shop.utils;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.category.ReadableCategory;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.shop.Breadcrumb;
import com.coretex.shop.model.shop.BreadcrumbItem;
import com.coretex.shop.model.shop.BreadcrumbItemType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@Component
public class BreadcrumbsUtils {

	@Resource
	private LabelUtils messages;

	@Resource
	private CategoryService categoryService;

	@Resource
	private FilePathUtils filePathUtils;


	public Breadcrumb buildCategoryBreadcrumb(ReadableCategory categoryClicked, MerchantStoreItem store, LanguageItem language, String contextPath) throws Exception {

		/** Rebuild breadcrumb **/
		BreadcrumbItem home = new BreadcrumbItem();
		home.setItemType(BreadcrumbItemType.HOME);
		home.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, LocaleUtils.getLocale(language)));
		home.setUrl(Constants.SHOP_URI);
		Breadcrumb breadCrumb = new Breadcrumb();
		breadCrumb.setLanguage(language);

		List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
		items.add(home);

		//if(!StringUtils.isBlank(refContent)) {

		//List<String> categoryIds = parseBreadCrumb(refContent);
		List<String> categoryIds = parseCategoryLineage(categoryClicked.getLineage());
		List<UUID> ids = new ArrayList<>();
		for (String c : categoryIds) {
			ids.add(UUID.fromString(c));
		}

		ids.add(categoryClicked.getUuid());


		List<CategoryItem> categories = categoryService.listByIds(store, ids, language);

		//category path - use lineage
		for (CategoryItem c : categories) {
			BreadcrumbItem categoryBreadcrump = new BreadcrumbItem();
			categoryBreadcrump.setItemType(BreadcrumbItemType.CATEGORY);
			categoryBreadcrump.setLabel(c.getName());
			categoryBreadcrump.setUrl(filePathUtils.buildCategoryUrl(store, contextPath, c.getSeUrl()));
			items.add(categoryBreadcrump);
		}

		breadCrumb.setUrlRefContent(buildBreadCrumb(ids));

		//}


		breadCrumb.setBreadCrumbs(items);
		breadCrumb.setItemType(BreadcrumbItemType.CATEGORY);


		return breadCrumb;
	}


	public Breadcrumb buildProductBreadcrumb(String refContent, ReadableProduct productClicked, MerchantStoreItem store, LanguageItem language, String contextPath) throws Exception {

		/** Rebuild breadcrumb **/
		BreadcrumbItem home = new BreadcrumbItem();
		home.setItemType(BreadcrumbItemType.HOME);
		home.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, LocaleUtils.getLocale(language)));
		home.setUrl(Constants.SHOP_URI);

		Breadcrumb breadCrumb = new Breadcrumb();
		breadCrumb.setLanguage(language);

		List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
		items.add(home);

		if (!StringUtils.isBlank(refContent)) {

			List<String> categoryIds = parseBreadCrumb(refContent);
			List<UUID> ids = new ArrayList<>();
			for (String c : categoryIds) {
				ids.add(UUID.fromString(c));
			}


			List<CategoryItem> categories = categoryService.listByIds(store, ids, language);

			//category path - use lineage
			for (CategoryItem c : categories) {
				BreadcrumbItem categoryBreadcrump = new BreadcrumbItem();
				categoryBreadcrump.setItemType(BreadcrumbItemType.CATEGORY);
				categoryBreadcrump.setLabel(c.getName());
				categoryBreadcrump.setUrl(filePathUtils.buildCategoryUrl(store, contextPath, c.getSeUrl()));
				items.add(categoryBreadcrump);
			}


			breadCrumb.setUrlRefContent(buildBreadCrumb(ids));
		}

		BreadcrumbItem productBreadcrump = new BreadcrumbItem();
		productBreadcrump.setItemType(BreadcrumbItemType.PRODUCT);
		productBreadcrump.setLabel(productClicked.getDescription().getName());
		productBreadcrump.setUrl(filePathUtils.buildProductUrl(store, contextPath, productClicked.getDescription().getFriendlyUrl()));
		items.add(productBreadcrump);


		breadCrumb.setBreadCrumbs(items);
		breadCrumb.setItemType(BreadcrumbItemType.CATEGORY);


		return breadCrumb;
	}

	@SuppressWarnings({"unchecked", "rawtypes", "unused"})
	private List<String> parseBreadCrumb(String refContent) throws Exception {

		/** c:1,2,3 **/
		String[] categoryComa = refContent.split(":");
		String[] categoryIds = categoryComa[1].split(",");
		return new LinkedList(Arrays.asList(categoryIds));


	}


	private List<String> parseCategoryLineage(String lineage) throws Exception {

		String[] categoryPath = lineage.split(Constants.CATEGORY_LINEAGE_DELIMITER);
		List<String> returnList = new LinkedList<String>();
		for (String c : categoryPath) {
			if (!StringUtils.isBlank(c)) {
				returnList.add(c);
			}
		}
		return returnList;

	}

	private String buildBreadCrumb(List<UUID> ids) throws Exception {

		if (CollectionUtils.isEmpty(ids)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("c:");
		int count = 1;
		for (UUID c : ids) {
			sb.append(c);
			if (count < ids.size()) {
				sb.append(",");
			}
			count++;
		}


		return sb.toString();

	}

}
