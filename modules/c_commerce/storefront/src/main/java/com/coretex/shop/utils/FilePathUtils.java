package com.coretex.shop.utils;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.coretex.items.commerce_core_model.DigitalProductItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.core.model.content.FileContentType;
import com.coretex.shop.constants.ApplicationConstants;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.order.ReadableOrderProductDownload;

@Component
public class FilePathUtils {

	@Resource
	private CoreConfiguration coreConfiguration;

	private static final String CONTEXT_PATH = "CONTEXT_PATH";


	public @Resource(name = "goodmood-properties")
	Properties properties = new Properties();//shopizer-properties

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;


	private final static String DOWNLOADS = "/downloads/";

	private final static String DOUBLE_SLASH = "://";


	/**
	 * Builds a static content content file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticFilePath(MerchantStoreItem store, String fileName) {
		StringBuilder sb = new StringBuilder().append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH);
		if (!StringUtils.isBlank(fileName)) {
			sb.append(fileName);
		}
		return sb.toString();
	}

	/**
	 * @param store
	 * @param fileName
	 * @return
	 */
	public String buildStaticFilePath(MerchantStoreItem store) {
		StringBuilder sb = new StringBuilder().append(Constants.STATIC_URI).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH);
		return sb.toString();
	}

	public String buildAdminDownloadProductFilePath(MerchantStoreItem store, DigitalProductItem digitalProduct) {
		return new StringBuilder().append(Constants.ADMIN_URI).append(Constants.FILES_URI).append(DOWNLOADS).append(store.getCode()).append(Constants.SLASH).append(digitalProduct.getProductFileName()).toString();
	}

	public String buildOrderDownloadProductFilePath(MerchantStoreItem store, ReadableOrderProductDownload digitalProduct, Long orderId) {
		return new StringBuilder().append(Constants.SHOP_URI).append(Constants.ORDER_DOWNLOAD_URI).append(Constants.SLASH).append(orderId).append(Constants.SLASH).append(digitalProduct.getUuid()).append(Constants.URL_EXTENSION).toString();
	}

	public String buildStaticFileAbsolutePath(MerchantStoreItem store, String fileName) {

		String absolutePath = null;

		if (!StringUtils.isBlank(imageUtils.getBasePath()) &&
				imageUtils.getBasePath().startsWith(Constants.HTTP_SCHEME)) {
			StringBuilder filePath = new StringBuilder();
			filePath.append(imageUtils.getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append(Constants.SLASH).append(FileContentType.STATIC_FILE).append(Constants.SLASH).append(fileName).toString();
			absolutePath = filePath.toString();
		} else {

			//Map<String,String> configurations = (Map<String, String>)request.getSession().getAttribute(Constants.STORE_CONFIGURATION);
			String scheme = Constants.HTTP_SCHEME;
			if (coreConfiguration != null) {
				scheme = coreConfiguration.getProperty("SHOP_SCHEME");
			}

			StringBuilder storePath = new StringBuilder();
			storePath.append(scheme).append("://")

					.append(store.getDomainName())
					.append(coreConfiguration.getProperty("CONTEXT_PATH"));


			//storePath.append(storePath.toString()).append(buildStaticFilePath(store,fileName));
			//absolutePath = storePath.toString();

			storePath.append(buildStaticFilePath(store, fileName));
			absolutePath = storePath.toString();

		}

		return absolutePath;


	}

	/**
	 * Builds http[s]://<domain name>/<context path>
	 *
	 * @param store
	 * @param request
	 * @return
	 */
	public String buildStoreUri(MerchantStoreItem store, HttpServletRequest request) {

		return this.buildBaseUrl(request, store);
	}

	public String buildStoreUri(MerchantStoreItem store, String contextPath) {

		StringBuilder resourcePath = new StringBuilder();

		String path = contextPath;
		if (Constants.SLASH.equals(path)) {
			path = Constants.BLANK;
		}

		String scheme = coreConfiguration.getProperty(ApplicationConstants.SHOP_SCHEME, "http");

		String domainName = store.getDomainName();
		if (StringUtils.isBlank(domainName)) {
			domainName = Constants.DEFAULT_DOMAIN_NAME;
		}

		resourcePath.append(scheme).append(DOUBLE_SLASH)
				.append(domainName)
				.append(path);

		return resourcePath.toString();

	}

	public String buildRelativeStoreUri(HttpServletRequest request, MerchantStoreItem store) {

		StringBuilder resourcePath = new StringBuilder();

		String path = request.getContextPath();
		if (Constants.SLASH.equals(path)) {
			path = Constants.BLANK;
		}

		resourcePath.append(path);

		return resourcePath.toString();

	}

	private String buildBaseUrl(HttpServletRequest request, MerchantStoreItem store) {
		StringBuilder resourcePath = new StringBuilder();

		String contextPath = request.getContextPath();
		if (Constants.SLASH.equals(contextPath)) {
			contextPath = Constants.BLANK;
		}

		String scheme = coreConfiguration.getProperty(ApplicationConstants.SHOP_SCHEME, "http");

		String domainName = store.getDomainName();
		if (StringUtils.isBlank(domainName)) {
			domainName = Constants.DEFAULT_DOMAIN_NAME;
		}

		resourcePath.append(scheme).append(DOUBLE_SLASH)
				.append(domainName)
				.append(contextPath);

		return resourcePath.toString();
	}


	/**
	 * Access to the customer section
	 *
	 * @param store
	 * @param request
	 * @return
	 */
	public String buildCustomerUri(MerchantStoreItem store, String contextPath) {

		return buildStoreUri(store, contextPath);
	}

	public String buildAdminUri(MerchantStoreItem store, HttpServletRequest request) {
		StringBuilder resourcePath = new StringBuilder();

		String baseUrl = this.buildBaseUrl(request, store);

		resourcePath
				.append(baseUrl)
				.append(Constants.ADMIN_URI);

		return resourcePath.toString();
	}

	public String buildCategoryUrl(MerchantStoreItem store, String contextPath, String url) {
		StringBuilder resourcePath = new StringBuilder();
		resourcePath.append(buildStoreUri(store, contextPath))

				.append(Constants.SHOP_URI)

				.append(Constants.CATEGORY_URI)
				.append(Constants.SLASH)
				.append(url)
				.append(Constants.URL_EXTENSION);

		return resourcePath.toString();

	}

	public String buildProductUrl(MerchantStoreItem store, String contextPath, String url) {
		StringBuilder resourcePath = new StringBuilder();
		resourcePath.append(buildStoreUri(store, contextPath))
				.append(Constants.SHOP_URI)
				.append(Constants.PRODUCT_URI)
				.append(Constants.SLASH)
				.append(url)
				.append(Constants.URL_EXTENSION);

		return resourcePath.toString();

	}

	public String getContextPath() {
		return properties.getProperty(CONTEXT_PATH);
	}


}
