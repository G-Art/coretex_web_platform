package com.coretex.shop.admin.controller.orders;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.core.LocaleItem;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.utils.ProductPriceUtils;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.core.model.common.CriteriaOrderBy;
import com.coretex.core.model.order.OrderCriteria;
import com.coretex.core.model.order.OrderList;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.DateUtil;
import com.coretex.shop.utils.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Manage order list
 * Manage search order
 *
 * @author csamson
 */
@Controller
@JsonAutoDetect(getterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE)
public class OrdersController {

	@Resource
	OrderService orderService;

	@Resource
	LabelUtils messages;

	@Resource
	private ProductPriceUtils priceUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);


	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/orders/list.html", method = RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		//the list of orders is from page method

		return ControllerConstants.Tiles.Order.orders;


	}


	@PreAuthorize("hasRole('ORDER')")
	@SuppressWarnings({"unchecked", "unused"})
	@RequestMapping(value = "/admin/orders/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	String pageOrders(HttpServletRequest request, HttpServletResponse response, Locale locale) {


		AjaxPageableResponse resp = new AjaxPageableResponse();

		try {

			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			String paymentModule = request.getParameter("paymentModule");
			String customerName = request.getParameter("customer");

			OrderCriteria criteria = new OrderCriteria();
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);
			if (!StringUtils.isBlank(paymentModule)) {
				criteria.setPaymentMethod(paymentModule);
			}

			if (!StringUtils.isBlank(customerName)) {
				criteria.setCustomerName(customerName);
			}

			LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			OrderList orderList = orderService.listByStore(store, criteria);

			if (orderList.getOrders() != null) {

				for (OrderItem order : orderList.getOrders()) {

					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("orderId", order.getUuid().toString());
					entry.put("customer", order.getDelivery().getFirstName());
					entry.put("amount", priceUtil.getAdminFormatedAmountWithCurrency(store, order.getTotal()));//todo format total
					entry.put("date", DateUtil.formatDate(order.getDatePurchased()));
					entry.put("status", order.getStatus().name());


					entry.put("paymentModule", paymentModule);
					resp.addDataEntry(entry);

				}
			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);


		} catch (Exception e) {
			LOGGER.error("Error while paging orders", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		return resp.toJSONString();
	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("order", "order");
		activeMenus.put("order-list", "order-list");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("order");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
