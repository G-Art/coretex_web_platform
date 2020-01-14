package com.coretex.shop.store.controller.order;

//import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.ZoneItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.order.ReadableOrder;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.store.controller.order.facade.OrderFacade;
import com.coretex.shop.utils.LabelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(Constants.SHOP_URI + "/order")
public class ShoppingOrderConfirmationController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShoppingOrderConfirmationController.class);

	@Resource
	private OrderService orderService;

	@Resource
	private CountryService countryService;

	@Resource
	private ZoneService zoneService;

	@Resource
	private OrderFacade orderFacade;

	@Resource
	private LabelUtils messages;

	/**
	 * Invoked once the payment is complete and order is fulfilled
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/confirmation.html")
	public String displayConfirmation(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		UUID orderId = super.getSessionAttribute(Constants.ORDER_ID, request);
		if (orderId == null) {
			return new StringBuilder().append("redirect:").append(Constants.SHOP_URI).toString();
		}

		//get the order
		OrderItem order = orderService.getByUUID(orderId);
		if (order == null) {
			LOGGER.warn("OrderItem id [" + orderId + "] does not exist");
			throw new Exception("OrderItem id [" + orderId + "] does not exist");
		}

		if (!order.getMerchant().getUuid().equals(store.getUuid())) {
			LOGGER.warn("Store id [" + store.getUuid() + "] differs from order.store.id [" + order.getMerchant().getUuid() + "]");
			return new StringBuilder().append("redirect:").append(Constants.SHOP_URI).toString();
		}

		if (super.getSessionAttribute(Constants.ORDER_ID_TOKEN, request) != null) {
			//set this unique token for performing unique operations in the confirmation
			model.addAttribute("confirmation", "confirmation");
		}

		//remove unique token
		super.removeAttribute(Constants.ORDER_ID_TOKEN, request);


		String[] orderMessageParams = {store.getStoreName()};
		String orderMessage = messages.getMessage("label.checkout.text", orderMessageParams, locale);
		model.addAttribute("ordermessage", orderMessage);

		String[] orderIdParams = {String.valueOf(order.getUuid())};
		String orderMessageId = messages.getMessage("label.checkout.orderid", orderIdParams, locale);
		model.addAttribute("ordermessageid", orderMessageId);

		String[] orderEmailParams = {order.getCustomerEmailAddress()};
		String orderEmailMessage = messages.getMessage("label.checkout.email", orderEmailParams, locale);
		model.addAttribute("orderemail", orderEmailMessage);

		ReadableOrder readableOrder = orderFacade.getReadableOrder(orderId, store, language);


		//resolve country and ZoneItem for GA
		String countryCode = readableOrder.getCustomer().getBilling().getCountry();
		Map<String, CountryItem> countriesMap = countryService.getCountriesMap(language);
		CountryItem billingCountry = countriesMap.get(countryCode);
		if (billingCountry != null) {
			readableOrder.getCustomer().getBilling().setCountry(billingCountry.getName());
		}

		String zoneCode = readableOrder.getCustomer().getBilling().getZone();
		Map<String, ZoneItem> zonesMap = zoneService.getZones(language);
		ZoneItem billingZone = zonesMap.get(zoneCode);
		if (billingZone != null) {
			readableOrder.getCustomer().getBilling().setZone(billingZone.getName());
		}


		model.addAttribute("order", readableOrder);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.confirmation).append(".").append(store.getStoreTemplate());
		return template.toString();


	}


}
