package com.coretex.shop.tags;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.core.LocaleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.business.utils.CacheUtils;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.utils.ImageFilePath;


public class ShopProductRelationshipTag extends RequestContextAwareTag {



	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ShopProductRelationshipTag.class);

	@Resource
	private ProductRelationshipService productRelationshipService;

	@Resource
	private PricingService pricingService;

	@Resource
	private CacheUtils cache;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;


	private String groupName;


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected int doStartTagInternal() throws Exception {
		if (productRelationshipService == null || pricingService == null || imageUtils == null) {
			LOGGER.debug("Autowiring ProductRelationshipService");
			WebApplicationContext wac = getRequestContext().getWebApplicationContext();
			AutowireCapableBeanFactory factory = wac.getAutowireCapableBeanFactory();
			factory.autowireBean(this);
		}

		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		LocaleItem language = (LocaleItem) request.getAttribute(Constants.LANGUAGE);

		StringBuilder groupKey = new StringBuilder();
		groupKey
				.append(store.getUuid())
				.append("_")
				.append(Constants.PRODUCTS_GROUP_CACHE_KEY)
				.append("-")
				.append(this.getGroupName())
				.append("_")
				.append(language.getIso());

		StringBuilder groupKeyMissed = new StringBuilder();
		groupKeyMissed
				.append(groupKey.toString())
				.append(Constants.MISSED_CACHE_KEY);

		List<ReadableProduct> objects = null;

		if (store.getUseCache()) {

			//get from the cache
			objects = (List<ReadableProduct>) cache.getFromCache(groupKey.toString());
			Boolean missedContent = null;

			if (objects == null && missedContent == null) {
				objects = getProducts(request);

				//put in cache
				cache.putInCache(objects, groupKey.toString());

			} else {
				//put in missed cache
				//cache.putInCache(new Boolean(true), groupKeyMissed.toString());
			}

		} else {
			objects = getProducts(request);
		}
		if (objects != null && objects.size() > 0) {
			request.setAttribute(this.getGroupName(), objects);
		}

		return SKIP_BODY;

	}


	public int doEndTag() {
		return EVAL_PAGE;
	}

	private List<ReadableProduct> getProducts(HttpServletRequest request) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		LocaleItem language = (LocaleItem) request.getAttribute(Constants.LANGUAGE);

		List<ProductRelationshipItem> relationships = productRelationshipService.getByGroup(store, this.getGroupName(), language);

		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);

		List<ReadableProduct> products = new ArrayList<ReadableProduct>();
		for (ProductRelationshipItem relationship : relationships) {

			ProductItem product = relationship.getRelatedProduct();

			ReadableProduct proxyProduct = populator.populate(product, new ReadableProduct(), store, language);
			products.add(proxyProduct);

		}

		return products;

	}


}
