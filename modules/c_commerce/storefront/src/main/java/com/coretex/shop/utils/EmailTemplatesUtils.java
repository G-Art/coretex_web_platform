package com.coretex.shop.utils;

import com.coretex.core.business.modules.email.Email;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.system.EmailService;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.shop.constants.ApplicationConstants;
import com.coretex.shop.constants.EmailConstants;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.shop.ContactForm;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


@Component
public class EmailTemplatesUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTemplatesUtils.class);

	@Resource
	private EmailService emailService;

	@Resource
	private LabelUtils messages;

	@Resource
	private CountryService countryService;

	@Resource
	private ProductService productService;

	@Resource
	private ZoneService zoneService;

	@Resource
	private PricingService pricingService;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@Resource
	private EmailUtils emailUtils;

	@Resource
	private FilePathUtils filePathUtils;

	private final static String LINE_BREAK = "<br/>";
	private final static String TABLE = "<table width=\"100%\">";
	private final static String CLOSING_TABLE = "</table>";
	private final static String TR = "<tr>";
	private final static String TR_BORDER = "<tr class=\"border\">";
	private final static String CLOSING_TR = "</tr>";
	private final static String TD = "<td valign=\"top\">";
	private final static String CLOSING_TD = "</td>";


	/**
	 * Sends an email to the customer after a completed order
	 *
	 * @param customer
	 * @param order
	 * @param customerLocale
	 * @param language
	 * @param merchantStore
	 * @param contextPath
	 */
	public void sendOrderEmail(String toEmail, CustomerItem customer, OrderItem order, Locale customerLocale, LocaleItem language, MerchantStoreItem merchantStore, String contextPath) {
		/** issue with putting that elsewhere **/
		LOGGER.info("Sending welcome email to customer");
		try {

			Map<String, ZoneItem> zones = zoneService.getZones(language);

			Map<String, CountryItem> countries = countryService.getCountriesMap(language);

			//format BillingItem address
			StringBuilder billing = new StringBuilder();
			if (StringUtils.isBlank(order.getBilling().getCompany())) {
				billing.append(order.getBilling().getFirstName()).append(" ")
						.append(order.getBilling().getLastName()).append(LINE_BREAK);
			} else {
				billing.append(order.getBilling().getCompany()).append(LINE_BREAK);
			}
			billing.append(order.getBilling().getAddress()).append(LINE_BREAK);
			billing.append(order.getBilling().getCity()).append(", ");

			if (order.getBilling().getZone() != null) {
				ZoneItem zone = zones.get(order.getBilling().getZone().getCode());
				if (zone != null) {
					billing.append(zone.getName());
				}
				billing.append(LINE_BREAK);
			} else if (!StringUtils.isBlank(order.getBilling().getState())) {
				billing.append(order.getBilling().getState()).append(LINE_BREAK);
			}
			CountryItem country = countries.get(order.getBilling().getCountry().getIsoCode());
			if (country != null) {
				billing.append(country.getName()).append(" ");
			}
			billing.append(order.getBilling().getPostalCode());


			//format shipping address
			StringBuilder shipping = null;
			if (order.getDelivery() != null && !StringUtils.isBlank(order.getDelivery().getFirstName())) {
				shipping = new StringBuilder();
				if (StringUtils.isBlank(order.getDelivery().getCompany())) {
					shipping.append(order.getDelivery().getFirstName()).append(" ")
							.append(order.getDelivery().getLastName()).append(LINE_BREAK);
				} else {
					shipping.append(order.getDelivery().getCompany()).append(LINE_BREAK);
				}
				shipping.append(order.getDelivery().getAddress()).append(LINE_BREAK);
				shipping.append(order.getDelivery().getCity()).append(", ");

				if (order.getDelivery().getZone() != null) {
					ZoneItem zone = zones.get(order.getDelivery().getZone().getCode());
					if (zone != null) {
						shipping.append(zone.getName());
					}
					shipping.append(LINE_BREAK);
				} else if (!StringUtils.isBlank(order.getDelivery().getState())) {
					shipping.append(order.getDelivery().getState()).append(LINE_BREAK);
				}
				CountryItem deliveryCountry = countries.get(order.getDelivery().getCountry().getIsoCode());
				if (country != null) {
					shipping.append(deliveryCountry.getName()).append(" ");
				}
				shipping.append(order.getDelivery().getPostalCode());
			}

			//format order
			//String storeUri = FilePathUtils.buildStoreUri(merchantStore, contextPath);
			StringBuilder orderTable = new StringBuilder();
			orderTable.append(TABLE);
			for (OrderProductItem product : order.getOrderProducts()) {
				//ProductItem productModel = productService.getByCode(product.getSku(), language);
				orderTable.append(TR);
				orderTable.append(TD).append(product.getProductName()).append(" - ").append(product.getSku()).append(CLOSING_TD);
				orderTable.append(TD).append(messages.getMessage("label.quantity", customerLocale)).append(": ").append(product.getProductQuantity()).append(CLOSING_TD);
				orderTable.append(TD).append(pricingService.getDisplayAmount(product.getOneTimeCharge(), merchantStore)).append(CLOSING_TD);
				orderTable.append(CLOSING_TR);
			}

			//order totals
			for (OrderTotalItem total : order.getOrderTotal()) {
				orderTable.append(TR_BORDER);
				//orderTable.append(TD);
				//orderTable.append(CLOSING_TD);
				orderTable.append(TD);
				orderTable.append(CLOSING_TD);
				orderTable.append(TD);
				orderTable.append("<strong>");

					orderTable.append(messages.getMessage(total.getOrderTotalCode(), customerLocale)).append(": ");
				orderTable.append("</strong>");
				orderTable.append(CLOSING_TD);
				orderTable.append(TD);
				orderTable.append("<strong>");

				orderTable.append(pricingService.getDisplayAmount(total.getValue(), merchantStore));

				orderTable.append("</strong>");
				orderTable.append(CLOSING_TD);
				orderTable.append(CLOSING_TR);
			}
			orderTable.append(CLOSING_TABLE);

			Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, order.getDelivery().getFirstName());
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, order.getDelivery().getLastName());

			String[] params = {String.valueOf(order.getUuid())};
			String[] dt = {DateUtil.formatDate(order.getDatePurchased())};
			templateTokens.put(EmailConstants.EMAIL_ORDER_NUMBER, messages.getMessage("email.order.confirmation", params, customerLocale));
			templateTokens.put(EmailConstants.EMAIL_ORDER_DATE, messages.getMessage("email.order.ordered", dt, customerLocale));
			templateTokens.put(EmailConstants.EMAIL_ORDER_THANKS, messages.getMessage("email.order.thanks", customerLocale));
			templateTokens.put(EmailConstants.ADDRESS_BILLING, billing.toString());

			templateTokens.put(EmailConstants.ORDER_PRODUCTS_DETAILS, orderTable.toString());
			templateTokens.put(EmailConstants.EMAIL_ORDER_DETAILS_TITLE, messages.getMessage("label.order.details", customerLocale));
			templateTokens.put(EmailConstants.ADDRESS_BILLING_TITLE, messages.getMessage("label.customer.billinginformation", customerLocale));
			templateTokens.put(EmailConstants.PAYMENT_METHOD_TITLE, messages.getMessage("label.order.paymentmode", customerLocale));


			templateTokens.put(EmailConstants.SHIPPING_METHOD_DETAILS, "");
			templateTokens.put(EmailConstants.ADDRESS_SHIPPING_TITLE, "");
			templateTokens.put(EmailConstants.ADDRESS_DELIVERY_TITLE, "");
			templateTokens.put(EmailConstants.SHIPPING_METHOD_TITLE, "");
			templateTokens.put(EmailConstants.ADDRESS_DELIVERY, "");

			String status = messages.getMessage("label.order." + order.getStatus().name(), customerLocale, order.getStatus().name());
			String[] statusMessage = {DateUtil.formatDate(order.getDatePurchased()), status};
			templateTokens.put(EmailConstants.ORDER_STATUS, messages.getMessage("email.order.status", statusMessage, customerLocale));


			String[] title = {merchantStore.getStoreName(), String.valueOf(order.getUuid())};
			Email email = new Email();
			email.setFrom(merchantStore.getStoreName());
			email.setFromEmail(merchantStore.getStoreEmailAddress());
			email.setSubject(messages.getMessage("email.order.title", title, customerLocale));
			email.setTo(toEmail);
			email.setTemplateName(EmailConstants.EMAIL_ORDER_TPL);
			email.setTemplateTokens(templateTokens);

			LOGGER.debug("Sending email to {} for order id {} ", customer.getEmail(), order.getUuid());
			emailService.sendHtmlEmail(merchantStore, email);

		} catch (Exception e) {
			LOGGER.error("Error occured while sending order confirmation email ", e);
		}

	}

	/**
	 * Sends an email to the customer after registration
	 *
	 * @param request
	 * @param customer
	 * @param merchantStore
	 * @param customerLocale
	 */
	public void sendRegistrationEmail(
			PersistableCustomer customer, MerchantStoreItem merchantStore,
			Locale customerLocale, String contextPath) {
		/** issue with putting that elsewhere **/
		LOGGER.info("Sending welcome email to customer");
		try {

			Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
			String[] greetingMessage = {merchantStore.getStoreName(), filePathUtils.buildCustomerUri(merchantStore, contextPath), merchantStore.getStoreEmailAddress()};
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_GREETING, messages.getMessage("email.customer.greeting", greetingMessage, customerLocale));
			templateTokens.put(EmailConstants.EMAIL_USERNAME_LABEL, messages.getMessage("label.generic.username", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password", customerLocale));
			templateTokens.put(EmailConstants.CUSTOMER_ACCESS_LABEL, messages.getMessage("label.customer.accessportal", customerLocale));
			templateTokens.put(EmailConstants.ACCESS_NOW_LABEL, messages.getMessage("label.customer.accessnow", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_USER_NAME, customer.getUserName());
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, customer.getClearPassword());

			//shop url
			String customerUrl = filePathUtils.buildStoreUri(merchantStore, contextPath);
			templateTokens.put(EmailConstants.CUSTOMER_ACCESS_URL, customerUrl);

			Email email = new Email();
			email.setFrom(merchantStore.getStoreName());
			email.setFromEmail(merchantStore.getStoreEmailAddress());
			email.setSubject(messages.getMessage("email.newuser.title", customerLocale));
			email.setTo(customer.getEmailAddress());
			email.setTemplateName(EmailConstants.EMAIL_CUSTOMER_TPL);
			email.setTemplateTokens(templateTokens);

			LOGGER.debug("Sending email to {} on their  registered email id {} ", customer.getBilling().getFirstName(), customer.getEmailAddress());
			emailService.sendHtmlEmail(merchantStore, email);

		} catch (Exception e) {
			LOGGER.error("Error occured while sending welcome email ", e);
		}

	}

	public void sendContactEmail(
			ContactForm contact, MerchantStoreItem merchantStore,
			Locale storeLocale, String contextPath) {
		/** issue with putting that elsewhere **/
		LOGGER.info("Sending welcome email to customer");
		try {

			Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, storeLocale);

			templateTokens.put(EmailConstants.EMAIL_CONTACT_NAME, contact.getName());
			templateTokens.put(EmailConstants.EMAIL_CONTACT_EMAIL, contact.getEmail());
			templateTokens.put(EmailConstants.EMAIL_CONTACT_CONTENT, contact.getComment());

			String[] contactSubject = {contact.getSubject()};

			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_CONTACT, messages.getMessage("email.contact", contactSubject, storeLocale));
			templateTokens.put(EmailConstants.EMAIL_CONTACT_NAME_LABEL, messages.getMessage("label.entity.name", storeLocale));
			templateTokens.put(EmailConstants.EMAIL_CONTACT_EMAIL_LABEL, messages.getMessage("label.generic.email", storeLocale));


			Email email = new Email();
			email.setFrom(merchantStore.getStoreName());
			email.setFromEmail(contact.getEmail());
			email.setSubject(messages.getMessage("email.contact.title", storeLocale));
			email.setTo(merchantStore.getStoreEmailAddress());
			email.setTemplateName(EmailConstants.EMAIL_CONTACT_TMPL);
			email.setTemplateTokens(templateTokens);

			LOGGER.debug("Sending contact email");
			emailService.sendHtmlEmail(merchantStore, email);

		} catch (Exception e) {
			LOGGER.error("Error occured while sending contact email ", e);
		}

	}

	/**
	 * Send an email to the customer with last order status
	 *
	 * @param request
	 * @param customer
	 * @param order
	 * @param merchantStore
	 * @param customerLocale
	 */
	public void sendUpdateOrderStatusEmail(
			CustomerItem customer, OrderItem order, OrderStatusHistoryItem lastHistory, MerchantStoreItem merchantStore,
			Locale customerLocale, String contextPath) {
		/** issue with putting that elsewhere **/
		LOGGER.info("Sending order status email to customer");
		try {


			Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);

			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());

			String[] statusMessageText = {String.valueOf(order.getUuid()), DateUtil.formatDate(order.getDatePurchased())};
			String status = messages.getMessage("label.order." + order.getStatus().name(), customerLocale, order.getStatus().name());
			String[] statusMessage = {DateUtil.formatDate(lastHistory.getDateAdded()), status};

			String comments = lastHistory.getComments();
			if (StringUtils.isBlank(comments)) {
				comments = messages.getMessage("label.order." + order.getStatus().name(), customerLocale, order.getStatus().name());
			}

			templateTokens.put(EmailConstants.EMAIL_ORDER_STATUS_TEXT, messages.getMessage("email.order.statustext", statusMessageText, customerLocale));
			templateTokens.put(EmailConstants.EMAIL_ORDER_STATUS, messages.getMessage("email.order.status", statusMessage, customerLocale));
			templateTokens.put(EmailConstants.EMAIL_TEXT_STATUS_COMMENTS, comments);


			Email email = new Email();
			email.setFrom(merchantStore.getStoreName());
			email.setFromEmail(merchantStore.getStoreEmailAddress());
			email.setSubject(messages.getMessage("email.order.status.title", new String[]{String.valueOf(order.getUuid())}, customerLocale));
			email.setTo(customer.getEmail());
			email.setTemplateName(EmailConstants.ORDER_STATUS_TMPL);
			email.setTemplateTokens(templateTokens);


			emailService.sendHtmlEmail(merchantStore, email);

		} catch (Exception e) {
			LOGGER.error("Error occured while sending order download email ", e);
		}

	}

	/**
	 * Send download email instructions to customer
	 *
	 * @param customer
	 * @param order
	 * @param merchantStore
	 * @param customerLocale
	 * @param contextPath
	 */
	public void sendOrderDownloadEmail(
			CustomerItem customer, OrderItem order, MerchantStoreItem merchantStore,
			Locale customerLocale, String contextPath) {
		/** issue with putting that elsewhere **/
		LOGGER.info("Sending download email to customer");
		try {

			Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
			String[] downloadMessage = {String.valueOf(ApplicationConstants.MAX_DOWNLOAD_DAYS), String.valueOf(order.getUuid()), filePathUtils.buildCustomerUri(merchantStore, contextPath), merchantStore.getStoreEmailAddress()};
			templateTokens.put(EmailConstants.EMAIL_ORDER_DOWNLOAD, messages.getMessage("email.order.download.text", downloadMessage, customerLocale));
			templateTokens.put(EmailConstants.CUSTOMER_ACCESS_LABEL, messages.getMessage("label.customer.accessportal", customerLocale));
			templateTokens.put(EmailConstants.ACCESS_NOW_LABEL, messages.getMessage("label.customer.accessnow", customerLocale));

			//shop url
			String customerUrl = filePathUtils.buildStoreUri(merchantStore, contextPath);
			templateTokens.put(EmailConstants.CUSTOMER_ACCESS_URL, customerUrl);

			String[] orderInfo = {String.valueOf(order.getUuid())};

			Email email = new Email();
			email.setFrom(merchantStore.getStoreName());
			email.setFromEmail(merchantStore.getStoreEmailAddress());
			email.setSubject(messages.getMessage("email.order.download.title", orderInfo, customerLocale));
			email.setTo(customer.getEmail());
			email.setTemplateName(EmailConstants.EMAIL_ORDER_DOWNLOAD_TPL);
			email.setTemplateTokens(templateTokens);

			LOGGER.debug("Sending email to {} with download info", customer.getEmail());
			emailService.sendHtmlEmail(merchantStore, email);

		} catch (Exception e) {
			LOGGER.error("Error occured while sending order download email ", e);
		}

	}

	/**
	 * Sends a change password notification email to the CustomerItem
	 *
	 * @param customer
	 * @param merchantStore
	 * @param customerLocale
	 * @param contextPath
	 */
	public void changePasswordNotificationEmail(
			CustomerItem customer, MerchantStoreItem merchantStore,
			Locale customerLocale, String contextPath) {
		LOGGER.debug("Sending change password email");
		try {


			Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);

			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
			templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());

			String[] date = {DateUtil.formatLongDate(new Date())};

			templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, messages.getMessage("label.notification.message.passwordchanged", date, customerLocale));


			Email email = new Email();
			email.setFrom(merchantStore.getStoreName());
			email.setFromEmail(merchantStore.getStoreEmailAddress());
			email.setSubject(messages.getMessage("label.notification.title.passwordchanged", customerLocale));
			email.setTo(customer.getEmail());
			email.setTemplateName(EmailConstants.EMAIL_NOTIFICATION_TMPL);
			email.setTemplateTokens(templateTokens);


			emailService.sendHtmlEmail(merchantStore, email);

		} catch (Exception e) {
			LOGGER.error("Error occured while sending change password email ", e);
		}

	}

}