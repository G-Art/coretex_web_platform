package com.coretex.shop.admin.controller.orders;

import com.coretex.core.business.modules.email.Email;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.services.order.orderproduct.OrderProductDownloadService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.system.EmailService;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.items.commerce_core_model.OrderProductDownloadItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.enums.commerce_core_model.PaymentTypeEnum;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.orders.OrderConverterHelper;
import com.coretex.core.data.orders.OrderForm;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.constants.EmailConstants;
import com.coretex.shop.utils.DateUtil;
import com.coretex.shop.utils.EmailUtils;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Manage order details
 *
 * @author Carl Samson
 */
@Controller
public class OrderControler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);

	@Resource
	private LabelUtils messages;

	@Resource
	private OrderService orderService;

	@Resource
	CountryService countryService;

	@Resource
	ZoneService zoneService;

	@Resource
	CustomerService customerService;

	@Resource
	PricingService pricingService;

	@Resource
	EmailService emailService;

	@Resource
	private EmailUtils emailUtils;

	@Resource
	OrderProductDownloadService orderProdctDownloadService;

	private final static String ORDER_STATUS_TMPL = "email_template_order_status.ftl";


	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/orders/editOrder.html", method = RequestMethod.GET)
	public String displayOrderEdit(@RequestParam("id") String orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayOrder(orderId, model, request, response);

	}

	@PreAuthorize("hasRole('ORDER')")
	private String displayOrder(String orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model, request);

		OrderForm order = new OrderForm();
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		List<CountryItem> countries = countryService.getCountries(language);
		if (orderId != null) {        //edit mode


			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


			Set<OrderProductItem> orderProducts = null;
			Set<OrderTotalItem> orderTotal = null;
			Set<OrderStatusHistoryItem> orderHistory = null;

			OrderItem dbOrder = orderService.getByUUID(UUID.fromString(orderId));

			if (dbOrder == null) {
				return "redirect:/admin/orders/list.html";
			}


			if (!dbOrder.getMerchant().getUuid().equals(store.getUuid())) {
				return "redirect:/admin/orders/list.html";
			}


			order.setUuid(orderId);

			if (dbOrder.getDatePurchased() != null) {
				order.setDatePurchased(DateUtil.formatDate(dbOrder.getDatePurchased()));
			}

			UUID customerId = dbOrder.getCustomerId();

			if (customerId != null) {

				try {

					CustomerItem customer = customerService.getByUUID(customerId);
					if (customer != null) {
						model.addAttribute("customer", customer);
					}


				} catch (Exception e) {
					LOGGER.error("Error while getting customer for customerId " + customerId, e);
				}

			}

			order.setCustomerId(dbOrder.getCustomerId().toString());
			order.setCustomerEmailAddress(dbOrder.getCustomerEmailAddress());
			order.setPaymentModuleCode(dbOrder.getPaymentModuleCode());
			order.setStatus(dbOrder.getStatus());
			order.setShippingModuleCode(dbOrder.getShippingModuleCode());
			order.setDelivery(OrderConverterHelper.convertDelivery(dbOrder.getDelivery()));

			order.setOrderHistory(dbOrder.getOrderHistory());
			order.setOrderProducts(dbOrder.getOrderProducts());
			order.setOrderTotal(dbOrder.getOrderTotal());
			order.setTotal(dbOrder.getTotal());


			orderProducts = dbOrder.getOrderProducts();
			orderTotal = dbOrder.getOrderTotal();
			orderHistory = dbOrder.getOrderHistory();

			//get capturable
//			if (dbOrder.getPaymentType() != PaymentTypeEnum.MONEYORDER) {
//				TransactionItem capturableTransaction = transactionService.getCapturableTransaction(dbOrder);
//				if (capturableTransaction != null) {
//					model.addAttribute("capturableTransaction", capturableTransaction);
//				}
//			}


			//get refundable
//			if (!dbOrder.getPaymentType().name().equals(PaymentTypeEnum.MONEYORDER.name())) {
//				TransactionItem refundableTransaction = transactionService.getRefundableTransaction(dbOrder);
//				if (refundableTransaction != null) {
//					model.addAttribute("capturableTransaction", null);//remove capturable
//					model.addAttribute("refundableTransaction", refundableTransaction);
//				}
//			}


			List<OrderProductDownloadItem> orderProductDownloads = orderProdctDownloadService.getByOrderId(UUID.fromString(order.getUuid()));
			if (CollectionUtils.isNotEmpty(orderProductDownloads)) {
				model.addAttribute("downloads", orderProductDownloads);
			}

		}

		model.addAttribute("countries", countries);
		model.addAttribute("order", order);
		return ControllerConstants.Tiles.Order.ordersEdit;
	}


	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/orders/save.html", method = RequestMethod.POST)
	public String saveOrder(@Valid @ModelAttribute("order") OrderForm entityOrder, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {

		String email_regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		Pattern pattern = Pattern.compile(email_regEx);

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		List<CountryItem> countries = countryService.getCountries(language);
		model.addAttribute("countries", countries);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		model.addAttribute("order", entityOrder);

		Set<OrderProductItem> orderProducts = new HashSet<OrderProductItem>();
		Set<OrderTotalItem> orderTotal = new HashSet<OrderTotalItem>();
		Set<OrderStatusHistoryItem> orderHistory = new HashSet<OrderStatusHistoryItem>();

		Date date = new Date();
		if (!StringUtils.isBlank(entityOrder.getDatePurchased())) {
			try {
				date = DateUtil.getDate(entityOrder.getDatePurchased());
			} catch (Exception e) {
				ObjectError error = new ObjectError("datePurchased", messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}

		} else {
			date = null;
		}


		if (!StringUtils.isBlank(entityOrder.getCustomerEmailAddress())) {
			java.util.regex.Matcher matcher = pattern.matcher(entityOrder.getCustomerEmailAddress());

			if (!matcher.find()) {
				ObjectError error = new ObjectError("customerEmailAddress", messages.getMessage("Email.order.customerEmailAddress", locale));
				result.addError(error);
			}
		} else {
			ObjectError error = new ObjectError("customerEmailAddress", messages.getMessage("NotEmpty.order.customerEmailAddress", locale));
			result.addError(error);
		}


		if (StringUtils.isBlank(entityOrder.getBilling().getFirstName())) {
			ObjectError error = new ObjectError("billingFirstName", messages.getMessage("NotEmpty.order.billingFirstName", locale));
			result.addError(error);
		}

		if (StringUtils.isBlank(entityOrder.getBilling().getFirstName())) {
			ObjectError error = new ObjectError("billingLastName", messages.getMessage("NotEmpty.order.billingLastName", locale));
			result.addError(error);
		}

		if (StringUtils.isBlank(entityOrder.getBilling().getAddress())) {
			ObjectError error = new ObjectError("billingAddress", messages.getMessage("NotEmpty.order.billingStreetAddress", locale));
			result.addError(error);
		}

		if (StringUtils.isBlank(entityOrder.getBilling().getCity())) {
			ObjectError error = new ObjectError("billingCity", messages.getMessage("NotEmpty.order.billingCity", locale));
			result.addError(error);
		}

		if (entityOrder.getBilling().getZone() == null) {
			if (StringUtils.isBlank(entityOrder.getBilling().getState())) {
				ObjectError error = new ObjectError("billingState", messages.getMessage("NotEmpty.order.billingState", locale));
				result.addError(error);
			}
		}

		if (StringUtils.isBlank(entityOrder.getBilling().getPostalCode())) {
			ObjectError error = new ObjectError("billingPostalCode", messages.getMessage("NotEmpty.order.billingPostCode", locale));
			result.addError(error);
		}

		OrderItem newOrder = orderService.getByUUID(UUID.fromString(entityOrder.getUuid()));

		if (result.hasErrors()) {
			//  somehow we lose data, so reset OrderItem detail info.
//			entityOrder.setOrderProducts(orderProducts);
//			entityOrder.setOrderTotal(orderTotal);
//			entityOrder.setOrderHistory(orderHistory);

			return ControllerConstants.Tiles.Order.ordersEdit;
			/*	"admin-orders-edit";  */
		}

		OrderStatusHistoryItem orderStatusHistory = new OrderStatusHistoryItem();


		CountryItem deliveryCountry = countryService.getByCode(entityOrder.getDelivery().getCountry().getIsoCode());
		CountryItem billingCountry = countryService.getByCode(entityOrder.getBilling().getCountry().getIsoCode());
		ZoneItem billingZone = null;
		ZoneItem deliveryZone = null;
		if (entityOrder.getBilling().getZone() != null) {
			billingZone = zoneService.getByCode(entityOrder.getBilling().getZone().getCode());
		}

		if (entityOrder.getDelivery().getZone() != null) {
			deliveryZone = zoneService.getByCode(entityOrder.getDelivery().getZone().getCode());
		}

		newOrder.setCustomerEmailAddress(entityOrder.getCustomerEmailAddress());
		newOrder.setStatus(entityOrder.getStatus());

		newOrder.setDatePurchased(date);
		newOrder.setLastModified(new Date());

		if (!StringUtils.isBlank(entityOrder.getOrderHistoryComment())) {
			orderStatusHistory.setComments(entityOrder.getOrderHistoryComment());
			orderStatusHistory.setCustomerNotified(1);
			orderStatusHistory.setStatus(entityOrder.getStatus());
			orderStatusHistory.setDateAdded(new Date());
			orderStatusHistory.setOrder(newOrder);
			newOrder.getOrderHistory().add(orderStatusHistory);
			entityOrder.setOrderHistoryComment("");
		}

		newOrder.setDelivery(OrderConverterHelper.convertBillingItem(entityOrder.getDelivery()));
		newOrder.setBilling(OrderConverterHelper.convertDeliveryItem(entityOrder.getBilling()));
		newOrder.setCustomerAgreement(entityOrder.getCustomerAgreement());

		newOrder.getDelivery().setCountry(deliveryCountry);
		newOrder.getBilling().setCountry(billingCountry);

		if (billingZone != null) {
			newOrder.getBilling().setZone(billingZone);
		}

		if (deliveryZone != null) {
			newOrder.getDelivery().setZone(deliveryZone);
		}

		orderService.saveOrUpdate(newOrder);
//		entityOrder.setOrder(newOrder);
		entityOrder.setBilling(OrderConverterHelper.convertBilling(newOrder.getBilling()));
		entityOrder.setDelivery(OrderConverterHelper.convertDelivery(newOrder.getDelivery()));
		entityOrder.setOrderTotal(newOrder.getOrderTotal());
		entityOrder.setOrderProducts(newOrder.getOrderProducts());
		entityOrder.setOrderHistory(newOrder.getOrderHistory());
		model.addAttribute("order", entityOrder);

		UUID customerId = newOrder.getCustomerId();

		if (customerId != null) {

			try {

				CustomerItem customer = customerService.getByUUID(customerId);
				if (customer != null) {
					model.addAttribute("customer", customer);
				}


			} catch (Exception e) {
				LOGGER.error("Error while getting customer for customerId " + customerId, e);
			}

		}

		List<OrderProductDownloadItem> orderProductDownloads = orderProdctDownloadService.getByOrderId(newOrder.getUuid());
		if (CollectionUtils.isNotEmpty(orderProductDownloads)) {
			model.addAttribute("downloads", orderProductDownloads);
		}


		/**
		 * send email if admin posted orderHistoryComment
		 *
		 * **/

		if (StringUtils.isBlank(entityOrder.getOrderHistoryComment())) {

			try {

				CustomerItem customer = customerService.getByUUID(newOrder.getCustomerId());
				LocaleItem lang = store.getDefaultLanguage();
				if (customer != null) {
					lang = customer.getDefaultLanguage();
				}

				Locale customerLocale = LocaleUtils.getLocale(lang);

				StringBuilder customerName = new StringBuilder();
				customerName.append(newOrder.getBilling().getFirstName()).append(" ").append(newOrder.getBilling().getLastName());


				Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, customerLocale);
				templateTokens.put(EmailConstants.EMAIL_CUSTOMER_NAME, customerName.toString());
				templateTokens.put(EmailConstants.EMAIL_TEXT_ORDER_NUMBER, messages.getMessage("email.order.confirmation", new String[]{String.valueOf(newOrder.getUuid())}, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_TEXT_DATE_ORDERED, messages.getMessage("email.order.ordered", new String[]{entityOrder.getDatePurchased()}, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_TEXT_STATUS_COMMENTS, messages.getMessage("email.order.comments", new String[]{entityOrder.getOrderHistoryComment()}, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_TEXT_DATE_UPDATED, messages.getMessage("email.order.updated", new String[]{DateUtil.formatDate(new Date())}, customerLocale));


				Email email = new Email();
				email.setFrom(store.getStoreName());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("email.order.status.title", new String[]{String.valueOf(newOrder.getUuid())}, customerLocale));
				email.setTo(entityOrder.getCustomerEmailAddress());
				email.setTemplateName(ORDER_STATUS_TMPL);
				email.setTemplateTokens(templateTokens);


				emailService.sendHtmlEmail(store, email);

			} catch (Exception e) {
				LOGGER.error("Cannot send email to customer", e);
			}

		}

		model.addAttribute("success", "success");


		return ControllerConstants.Tiles.Order.ordersEdit;
		/*	"admin-orders-edit";  */
	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("order", "order");
		activeMenus.put("order-list", "order-list");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		model.addAttribute("activeMenus", activeMenus);

		Menu currentMenu = menus.get("order");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
