package com.coretex.shop.store.controller.customer;

import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.order.orderproduct.OrderProductDownloadService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderProductDownloadItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.order.ReadableOrder;
import com.coretex.shop.model.order.ReadableOrderList;
import com.coretex.shop.model.order.ReadableOrderProductDownload;
import com.coretex.shop.populator.order.ReadableOrderProductDownloadPopulator;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.store.controller.customer.facade.CustomerFacade;
import com.coretex.shop.store.controller.order.facade.OrderFacade;
import com.coretex.shop.store.model.paging.PaginationData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(Constants.SHOP_URI + "/customer")
public class CustomerOrdersController extends AbstractController {

	@Resource
	private OrderFacade orderFacade;

	@Resource
	private CustomerFacade customerFacade;

	@Resource
	private OrderProductDownloadService orderProdctDownloadService;


	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOrdersController.class);

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/orders.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String listOrders(Model model, @RequestParam(value = "page", defaultValue = "1") final int page, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LOGGER.info("Fetching orders for current customer");
		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		LocaleItem language = getSessionAttribute(Constants.LANGUAGE, request);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerItem customer = null;
		if (auth != null &&
				request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getCustomerByUserName(auth.getName(), store);

		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}

		PaginationData paginaionData = createPaginaionData(page, Constants.MAX_ORDERS_PAGE);
		ReadableOrderList readable = orderFacade.getReadableOrderList(store, customer, (paginaionData.getOffset() - 1), paginaionData.getPageSize(), language);

		model.addAttribute("customerOrders", readable);
		if (readable != null) {
			model.addAttribute("paginationData", calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, readable.getTotal()));
		} else {
			model.addAttribute("paginationData", null);
		}


		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerOrders).append(".").append(store.getStoreTemplate());
		return template.toString();
	}


	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/order.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String orderDetails(final Model model, final HttpServletRequest request, @RequestParam(value = "orderId", required = true) final String orderId) throws Exception {

		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);

		LocaleItem language = (LocaleItem) request.getAttribute(Constants.LANGUAGE);

		if (StringUtils.isBlank(orderId)) {
			LOGGER.error("OrderItem Id can not be null or empty");
		}
		LOGGER.info("Fetching order details for Id " + orderId);

		//get order id
		UUID lOrderId = null;
		try {
			lOrderId = UUID.fromString(orderId);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Cannot parse orderId to long " + orderId);
			return "redirect:/" + Constants.SHOP_URI;
		}


		//check if order belongs to customer logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerItem customer = null;
		if (auth != null &&
				request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getCustomerByUserName(auth.getName(), store);

		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}

		ReadableOrder order = orderFacade.getReadableOrder(lOrderId, store, customer.getDefaultLanguage());

		model.addAttribute("order", order);

		//check if any downloads exist for this order
		List<OrderProductDownloadItem> orderProductDownloads = orderProdctDownloadService.getByOrderId(order.getUuid());
		if (CollectionUtils.isNotEmpty(orderProductDownloads)) {
			ReadableOrderProductDownloadPopulator populator = new ReadableOrderProductDownloadPopulator();
			List<ReadableOrderProductDownload> downloads = new ArrayList<ReadableOrderProductDownload>();
			for (OrderProductDownloadItem download : orderProductDownloads) {
				ReadableOrderProductDownload view = new ReadableOrderProductDownload();
				populator.populate(download, view, store, language);
				downloads.add(view);
			}
			model.addAttribute("downloads", downloads);
		}

		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerOrder).append(".").append(store.getStoreTemplate());
		return template.toString();

	}
}
